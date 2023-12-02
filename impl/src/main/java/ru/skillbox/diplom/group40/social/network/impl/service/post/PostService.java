package ru.skillbox.diplom.group40.social.network.impl.service.post;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skillbox.diplom.group40.social.network.api.dto.account.AccountDto;
import ru.skillbox.diplom.group40.social.network.api.dto.account.AccountSearchDto;
import ru.skillbox.diplom.group40.social.network.api.dto.base.BaseDto;
import ru.skillbox.diplom.group40.social.network.api.dto.post.PostDto;
import ru.skillbox.diplom.group40.social.network.api.dto.post.PostSearchDto;
import ru.skillbox.diplom.group40.social.network.api.dto.post.*;
import ru.skillbox.diplom.group40.social.network.api.dto.post.Type;
import ru.skillbox.diplom.group40.social.network.api.dto.search.BaseSearchDto;
import ru.skillbox.diplom.group40.social.network.domain.post.*;
import ru.skillbox.diplom.group40.social.network.impl.exception.NotFoundException;
import ru.skillbox.diplom.group40.social.network.impl.mapper.notification.NotificationsMapper;
import ru.skillbox.diplom.group40.social.network.impl.mapper.post.CommentMapper;
import ru.skillbox.diplom.group40.social.network.impl.mapper.post.PostMapper;
import ru.skillbox.diplom.group40.social.network.impl.repository.post.PostRepository;
import ru.skillbox.diplom.group40.social.network.impl.service.account.AccountService;
import ru.skillbox.diplom.group40.social.network.impl.service.kafka.KafkaService;
import ru.skillbox.diplom.group40.social.network.impl.utils.auth.AuthUtil;
import ru.skillbox.diplom.group40.social.network.impl.utils.specification.SpecificationUtils;

import javax.security.auth.login.AccountException;
import java.time.ZonedDateTime;
import java.util.*;

/**
 * PostService
 *
 * @author Sergey D.
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class PostService {

    private final PostMapper postMapper;
    private final PostRepository postRepository;
    private final CommentService commentService;
    private final CommentMapper commentMapper;
    private final LikeService likeService;
    private final AccountService accountService;
    private final NotificationsMapper notificationsMapper;
    private final KafkaService kafkaService;
    private String notFoundMessage = "Пользователь не найден";

    public PostDto create(PostDto postDto) {
        log.info("PostService: create(PostDto postDto), title = " + postDto.getTitle() + " (Start method) ->" + postDto.getPublishDate());
        postDto.setAuthorId(AuthUtil.getUserId());

        Post post = new Post();
        Post currentPost = postRepository.save(post);
        post = postMapper.toPost(postDto);
        post.setId(currentPost.getId());
        if (post.getType().equals(Type.POSTED)) createNotificationForPost(postDto);
        return postMapper.toDto(postRepository.save(post));
    }

    public PostDto update(PostDto postDto) {
        log.info("PostService: update(PostDto postDto), postDto = " + postDto + " (Start method)");
        Post currentPost = postRepository.findById(postDto.getId()).orElseThrow(()
                -> new NotFoundException(notFoundMessage));

        return postMapper.toDto(postRepository.save(postMapper.toPost(postDto, currentPost)));
    }

    public PostDto get(UUID id) {
        log.info("PostService: get(UUID id), id = " + id + " (Start method)");
        return postMapper.toDto(postRepository.findById(id).orElseThrow(()
                -> new NotFoundException(notFoundMessage)));
    }


    public Page<PostDto> getAll(PostSearchDto postSearchDto, Pageable page) throws AccountException {
        log.info("PostService: getAll() Start method " + postSearchDto);

        BaseSearchDto baseSearchDto = new BaseSearchDto();
        baseSearchDto.setIsDeleted(postSearchDto.getIsDeleted());

        Specification postDtoSpecification = SpecificationUtils.getBaseSpecification(baseSearchDto)
                .and(SpecificationUtils.in(Post_.AUTHOR_ID, postSearchDto.getAccountIds()))
                .and(SpecificationUtils.in(Post_.ID, postSearchDto.getIds()))
                .and(SpecificationUtils.like(Post_.POST_TEXT, postSearchDto.getText()))
                .and(SpecificationUtils.betweenDate(Post_.PUBLISH_DATE, postSearchDto.getDateFrom(), postSearchDto.getDateTo()))
                .and(SpecificationUtils.in(Post_.AUTHOR_ID, uuidListFromAccount(postSearchDto)))
                .and(SpecificationUtils.containTag(Post_.TAGS, postSearchDto.getTags()));

        Page<Post> posts = postRepository.findAll(postDtoSpecification, page);

        Page<PostDto> postDtos = posts.map(postMapper::toDto);

//        for (PostDto postDto : postDtos) {
//            List<Like> likes = likeService.getByAuthorAndItem(AuthUtil.getUserId(), postDto.getId());
//            for (Like like : likes) {
//                if (!like.getIsDeleted()) {
//                    postDto.setMyLike(true);
//                    String reaction = like.getReactionType();
//                    postDto.setMyReaction(reaction);
//                    //postDto.setReactionType(reaction);
//                }
//            }
//        }
        for (PostDto postDto : postDtos){
            List<Like> likes = likeService.getByAuthorAndItem(AuthUtil.getUserId(), postDto.getId());
            for(Like like : likes) {
                if (!like.getIsDeleted()) {
                    postDto.setMyLike(true);
                    String reaction = like.getReactionType();
                    postDto.setMyReaction(reaction);
                    List<Like> likesForPost = likeService.getAllByItemId(postDto.getId());
                    List<String> reactions = new ArrayList<>();
                    Set<String> reactionTypes = new HashSet<>();
                    for (Like likeForPost : likesForPost){
                        if(!likeForPost.getIsDeleted()) {
                            String reactionType = likeForPost.getReactionType();
                            reactions.add(reactionType);
                            reactionTypes.add(reactionType);
                        }
                    }
                    List<LikeReaction> likeReactions = new ArrayList<>();
                    for(String type : reactionTypes){
                        likeReactions.add(new LikeReaction(type, Collections.frequency(reactions, type)));
                    }
                    postDto.setReactionType(likeReactions);
                }
            }
        }


        return postDtos;
    }

    public List<UUID> uuidListFromAccount(PostSearchDto postSearchDto) throws AccountException {
        AccountSearchDto accountSearchDto = new AccountSearchDto();
        accountSearchDto.setFirstName(postSearchDto.getAuthor());
        accountSearchDto.setLastName(postSearchDto.getAuthor());
        Page<AccountDto> accounts = accountService.getAll(accountSearchDto, Pageable.unpaged());

        return accounts.stream().map(BaseDto::getId).toList();
    }

    public void deleteById(UUID id) {
        log.info("PostService: deleteById(PostDto postDto), id = " + id + " (Start method)");
        Post post = postRepository.findById(id).orElseThrow(() -> new NotFoundException(notFoundMessage));
        post.setIsDeleted(true);
        List<Comment> comments = commentService.getAllByPatentId(id);
        for (Comment comment : comments) {
            deleteComment(id, comment.getId());
        }
        List<Like> likes = likeService.getAllByItemId(id);
        for (Like like : likes) {
            like.setIsDeleted(true);
            likeService.update(like);
            decLikeAmount(id);
        }
        postRepository.save(post);
    }

    public void incLikeAmount(UUID id) {
        log.info("PostService: +1 like for post with id: " + id);
        PostDto postDto = get(id);
        postDto.setLikeAmount(postDto.getLikeAmount() + 1);
        update(postDto);
    }

    public void decLikeAmount(UUID id) {
        log.info("PostService: -1 like for post with id " + id);
        PostDto postDto = get(id);
        postDto.setLikeAmount(postDto.getLikeAmount() - 1);
        update(postDto);
    }

    public void incCommentsCount(UUID id) {
        log.info("PostService: +1 comment for post with id " + id);
        PostDto postDto = get(id);
        postDto.setCommentsCount(postDto.getCommentsCount() + 1);
        update(postDto);
    }

    public void decCommentsCount(UUID id) {
        log.info("PostService: -1 comment for post with id " + id);
        PostDto postDto = get(id);
        postDto.setCommentsCount(postDto.getCommentsCount() - 1);
        update(postDto);
    }

    public Page<CommentDto> getPostComments(UUID postId, CommentSearchDto commentSearchDto, Pageable page) {
        log.info("PostService: get all comments for post " + postId);

        BaseSearchDto baseSearchDto = new BaseSearchDto();
        baseSearchDto.setIsDeleted(commentSearchDto.getIsDeleted());

        Specification commentDtoSpecification = SpecificationUtils.getBaseSpecification(baseSearchDto)
                .and(SpecificationUtils.in(Comment_.COMMENT_TYPE, CommentType.POST)
                        .and(SpecificationUtils.in(Comment_.AUTHOR_ID, commentSearchDto.getAuthorId()))
                        .and(SpecificationUtils.in(Comment_.PARENT_ID, commentSearchDto.getParentId()))
                        .and(SpecificationUtils.in(Comment_.POST_ID, commentSearchDto.getPostId())));
        Page<Comment> comments = commentService.getComments(commentDtoSpecification, page);

        Page<CommentDto> commentDtos = comments.map(commentMapper::modelToDto);
        for (CommentDto commentDto : commentDtos) {
            List<Like> likes = likeService.getByAuthorAndItem(AuthUtil.getUserId(), commentDto.getId());
            for (Like like : likes) {
                if (!like.getIsDeleted()) {
                    commentDto.setMyLike(true);
                }
            }
        }

        return commentDtos;
    }

    public Page<CommentDto> getSubcomments(UUID postId,
                                           UUID commentId,
                                           CommentSearchDto commentSearchDto, Pageable page) {
        log.info("PostService: get all comments for comments " + commentSearchDto);

        BaseSearchDto baseSearchDto = new BaseSearchDto();
        baseSearchDto.setIsDeleted(commentSearchDto.getIsDeleted());

        Specification commentDtoSpecification = SpecificationUtils.getBaseSpecification(baseSearchDto)
                .and(SpecificationUtils.in(Comment_.COMMENT_TYPE, CommentType.COMMENT)
                        .and(SpecificationUtils.in(Comment_.AUTHOR_ID, commentSearchDto.getAuthorId()))
                        .and(SpecificationUtils.in(Comment_.PARENT_ID, commentId)
                                .and(SpecificationUtils.in(Comment_.POST_ID, postId))));
        Page<Comment> comments = commentService.getComments(commentDtoSpecification, page);

        Page<CommentDto> commentDtos = comments.map(commentMapper::modelToDto);
        for (CommentDto commentDto : commentDtos) {
            List<Like> likes = likeService.getByAuthorAndItem(AuthUtil.getUserId(), commentDto.getId());
            for (Like like : likes) {
                if (!like.getIsDeleted()) {
                    commentDto.setMyLike(true);
                }
            }
        }

        return commentDtos;
    }

    public LikeDto createLikeForPost(UUID id, LikeDto response) {
        log.info("PostService: create like for post with id: " + id);

        PostDto postDto = get(id);
        LikeDto likeDto = likeService.createForPost(id, response);
        incLikeAmount(id);

        return likeDto;
    }

    public void deleteLikeForPost(UUID id) {
        log.info("PostService: delete like for post with id: " + id);

        PostDto postDto = get(id);
        likeService.deleteForPost(id);
        decLikeAmount(id);
    }

    public CommentDto createComment(CommentDto commentDto, UUID id) {
        log.info("PostService: create comment for post with id: " + id);
        CommentDto dto = commentService.create(commentDto, id);
        incCommentsCount(id);
        createNotificationForComment(dto);
        return dto;
    }

    public void deleteComment(UUID id, UUID commentId) {
        log.info("PostService: create comment for post with id: " + id);
        Integer commentsDeleted = commentService.delete(id, commentId);
        for (int i = 0; i < commentsDeleted; i++) {
            decCommentsCount(id);
        }
    }

    public void createNotificationForPost(PostDto postDto) {
        log.info("PostService: createNotification(PostDto postDto) startMethod, title = {}", postDto.getTitle());
        kafkaService.sendNotification(notificationsMapper.postToNotificationDTO(postDto));
    }

    public void createNotificationForComment(CommentDto commentDto) {
        log.info("PostService: createNotificationForComment(CommentDto commentDto) startMethod, ", commentDto);
        kafkaService.sendNotification(notificationsMapper.commentToNotificationDTO(commentDto));
    }

    @Scheduled(cron = "${cron.delayedPost}")
    public void delayed() {
        log.info("PostService: delayed " + ZonedDateTime.now() + " Start method");
        postRepository.findAllByTypeAndPublishDateBefore(Type.QUEUED, ZonedDateTime.now()).forEach(post -> {
            post.setType(Type.POSTED);
            postRepository.save(post);
            createNotificationForPost(postMapper.toDto(post));
        });
    }

}