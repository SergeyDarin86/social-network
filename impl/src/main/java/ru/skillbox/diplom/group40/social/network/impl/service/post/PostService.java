package ru.skillbox.diplom.group40.social.network.impl.service.post;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skillbox.diplom.group40.social.network.api.dto.post.PostDto;
import ru.skillbox.diplom.group40.social.network.api.dto.post.PostSearchDto;
import ru.skillbox.diplom.group40.social.network.api.dto.post.Type;
import ru.skillbox.diplom.group40.social.network.domain.post.Post;
import ru.skillbox.diplom.group40.social.network.impl.exception.NotFoundException;
import ru.skillbox.diplom.group40.social.network.impl.mapper.post.PostMapper;
import ru.skillbox.diplom.group40.social.network.impl.repository.post.PostRepository;
import ru.skillbox.diplom.group40.social.network.impl.utils.auth.AuthUtil;
import ru.skillbox.diplom.group40.social.network.impl.utils.specification.SpecificationUtils;

import java.time.LocalDateTime;
import java.util.UUID;

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

    private String notFoundMessage = "Пользователь не найден";

    public PostDto create(PostDto postDto) {
        log.info("PostService: save(PostDto postDto), id = " + postDto.getId() + " (Start method");
        postDto.setAuthorId(AuthUtil.getUserId());
        postDto.setType(Type.POSTED);
        postDto.setTime(LocalDateTime.now());
        Post post = postMapper.toPost(postDto);
        postRepository.save(post);
        return postMapper.toDto(post);
    }

    public PostDto update(PostDto postDto) {
        log.info("PostService: update(PostDto postDto), id = " + postDto.getId() + " (Start method)");
        postRepository.findById(postDto.getId()).orElseThrow(()
                -> new NotFoundException(notFoundMessage));
        Post currentPost = postMapper.toPost(postDto);
        currentPost.setAuthorId(AuthUtil.getUserId());
        currentPost.setType(Type.POSTED);
        currentPost.setTime(LocalDateTime.now());
        postRepository.save(currentPost);
        return postMapper.toDto(currentPost);
    }

    public PostDto get(UUID id) {
        log.info("PostService: get(UUID id), id = " + id + " (Start method)");
        return postMapper.toDto(postRepository.findById(id).orElseThrow(()
                -> new NotFoundException(notFoundMessage)));
    }

    public Page<PostDto> getAll(PostSearchDto postSearchDto, Pageable page) {
        log.info("PostService: getAll() Start method " + postSearchDto.toString());
        if (!isNullOrEmptyAccountIds(postSearchDto)) {
            Specification postDtoSpecification = SpecificationUtils
                    .equal("authorId", postSearchDto.getAccountIds().get(0))
                    .and(SpecificationUtils.like("postText", "qqqqq"));
            Page<Post> posts = postRepository.findAll(postDtoSpecification, page);
            return posts.map(postMapper::toDto);
        } else {
            Page<Post> posts = postRepository.findAll(page);
            return posts.map(postMapper::toDto);
        }

    }

    public static boolean isNullOrEmptyAccountIds(PostSearchDto postSearchDto) {
        return (postSearchDto.getAccountIds() == null || postSearchDto.getAccountIds().isEmpty());
    }

    public void deleteById(UUID id) {
        log.info("PostService: deleteById(PostDto postDto), id = " + id + " (Start method)");
        postRepository.findById(id).orElseThrow(() -> new NotFoundException(notFoundMessage));
        postRepository.deleteById(id);
    }

}
