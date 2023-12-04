package ru.skillbox.diplom.group40.social.network.impl.service.post;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import ru.skillbox.diplom.group40.social.network.api.dto.post.*;
import ru.skillbox.diplom.group40.social.network.api.dto.search.BaseSearchDto;
import ru.skillbox.diplom.group40.social.network.domain.post.Comment;
import ru.skillbox.diplom.group40.social.network.domain.post.Comment_;
import ru.skillbox.diplom.group40.social.network.domain.post.Like;
import ru.skillbox.diplom.group40.social.network.impl.exception.NotFoundException;
import ru.skillbox.diplom.group40.social.network.impl.mapper.post.CommentMapperImpl;
import ru.skillbox.diplom.group40.social.network.impl.repository.post.CommentRepository;
import ru.skillbox.diplom.group40.social.network.impl.utils.auth.AuthUtil;
import ru.skillbox.diplom.group40.social.network.impl.utils.specification.SpecificationUtils;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {
    private final CommentMapperImpl commentMapper;
    private final CommentRepository commentRepository;
    private  final LikeService likeService;
    public CommentDto get(UUID commentId) {
        log.info("CommentService: get comment with id: " + commentId);
        return commentMapper.modelToDto(commentRepository.findById(commentId).orElseThrow(()
                -> new NotFoundException("Comment not found")));
    }
    public CommentDto update(CommentDto commentDto){
        log.info("CommentService: update comment");
        Comment comment = commentRepository.findById(commentDto.getId()).orElseThrow(()
                -> new NotFoundException("Comment not found"));
        commentDto.setTimeChanged(ZonedDateTime.now());
        commentMapper.dtoToModel(commentDto, comment);
        commentRepository.save(comment);
        return commentMapper.modelToDto(comment);
    }

    public CommentDto updateSub(CommentDto commentDto, UUID commentId){
        log.info("CommentService: update subcomment");
        Comment comment = commentRepository.findById(commentDto.getId()).orElseThrow(()
                -> new NotFoundException("Comment not found"));
        commentDto.setTimeChanged(ZonedDateTime.now());
        commentDto.setParentId(commentId);
        commentMapper.dtoToModel(commentDto, comment);
        commentRepository.save(comment);
        return commentMapper.modelToDto(comment);
    }

    public CommentDto create(CommentDto commentDto, UUID id){
        log.info("CommentService: create comment");
        commentDto.setCommentsCount(0);
        commentDto.setTime(ZonedDateTime.now());
        commentDto.setTimeChanged(ZonedDateTime.now());
        commentDto.setIsBlocked(false);
        commentDto.setIsDeleted(false);
        commentDto.setAuthorId(AuthUtil.getUserId());
        commentDto.setPostId(id);
        commentDto.setLikeAmount(0);
        commentDto.setMyLike(false);
        UUID parentId = commentDto.getParentId();
        if(parentId == null){
            commentDto.setParentId(id);
            commentDto.setCommentType(CommentType.POST);
        } else{
            commentDto.setParentId(parentId);
            commentDto.setCommentType(CommentType.COMMENT);
            incCommentsCount(parentId);
        }
        Comment comment = commentMapper.dtoToModel(commentDto);
        commentRepository.save(comment);

        return commentMapper.modelToDto(comment);
    }

    public Integer delete(UUID id, UUID commentId){
        log.info("CommentService: delete comment with id" + commentId + " for post with id: " + commentId);

        Integer commentsDeleted = 0;
        BaseSearchDto baseSearchDto = new BaseSearchDto();

        Specification commentDtoSpecification = SpecificationUtils.getBaseSpecification(baseSearchDto)
                .and(SpecificationUtils.in(Comment_.AUTHOR_ID, AuthUtil.getUserId())
                        .and(SpecificationUtils.in(Comment_.ID, commentId)
                        .and(SpecificationUtils.in(Comment_.POST_ID, id))));
        Optional<Comment> commentOptional = commentRepository.findOne(commentDtoSpecification);
        if(commentOptional.isPresent()){
            Comment comment = commentOptional.get();
            if(comment.getCommentType() == CommentType.POST){
                List<Comment> subComments = commentRepository.findAllByParentId(commentId);
                for (Comment subComment : subComments){
                    if(!subComment.getIsDeleted()) {
                        deleteCommentWithLikes(subComment);
                        commentsDeleted++;
                    }
                }
            }
            deleteCommentWithLikes(comment);
            commentsDeleted++;
        }
        return commentsDeleted;
    }
    public void incLikeAmount(UUID commentId){
        log.info("CommentService: +1 like for comment with id: " + commentId );
        CommentDto commentDto = get(commentId);
        commentDto.setLikeAmount(commentDto.getLikeAmount() + 1);
        update(commentDto);
    }

    public void decLikeAmount(UUID commentId){
        log.info("CommentService: -1 like for comment with id: " + commentId );
        CommentDto commentDto = get(commentId);
        commentDto.setLikeAmount(commentDto.getLikeAmount() - 1);
        update(commentDto);
    }

    public void incCommentsCount(UUID commentId){
        log.info("CommentService: +1 subcomment for comment with id: " + commentId );
        CommentDto commentDto = get(commentId);
        commentDto.setCommentsCount(commentDto.getCommentsCount() + 1);
        update(commentDto);
    }

    public void decCommentsCount(UUID commentId){
        log.info("CommentService: -1 subcomment for comment with id: " + commentId );
        CommentDto commentDto = get(commentId);
        commentDto.setCommentsCount(commentDto.getCommentsCount() - 1);
        update(commentDto);
    }

    public void deleteCommentWithLikes(Comment comment){
        log.info("CommentService: delete comment with all its subcomments and their likes.");
        UUID commentId = comment.getId();
        List<Like> commentLikes = likeService.getAllByItemId(commentId);
        for (Like commentLike : commentLikes){
            if (!commentLike.getIsDeleted()) {
                commentLike.setIsDeleted(true);
                likeService.update(commentLike);
                decLikeAmount(commentId);
            }
        }
        if(comment.getCommentType() == CommentType.COMMENT) {
            decCommentsCount(comment.getParentId());
        }
        comment.setIsDeleted(true);
        commentRepository.save(comment);
    }
    public LikeDto createLikeForComment(UUID id, UUID commentId){
        log.info("LikeService: createForComment. CommentId: " + commentId);

        CommentDto commentDto = get(commentId);
        LikeDto likeDto = likeService.createForComment(id, commentId);
        incLikeAmount(commentId);

        return likeDto;
    }

    public void deleteLikeForComment(UUID id, UUID commentId){
        log.info("LikeService: createForComment. CommentId: " + commentId);

        CommentDto commentDto = get(commentId);
        likeService.deleteForComment(id, commentId);
        decLikeAmount(commentId);
    }

    public Page<Comment> getComments(Specification specification, Pageable pageable){
        return commentRepository.findAll(specification, pageable);
    }

    public Comment getByAuthorIdAndTime(UUID authorId, ZonedDateTime zonedDateTime) {return commentRepository.findByAuthorIdAndTime(authorId, zonedDateTime);}

    public List<Comment> getAllByPatentId(UUID postId){
        return commentRepository.findAllByParentId(postId);
    }

}
