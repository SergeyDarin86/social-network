package ru.skillbox.diplom.group40.social.network.impl.service.post;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skillbox.diplom.group40.social.network.api.dto.post.PostDto;
import ru.skillbox.diplom.group40.social.network.domain.post.Post;
import ru.skillbox.diplom.group40.social.network.impl.exception.NotFoundException;
import ru.skillbox.diplom.group40.social.network.impl.mapper.post.PostMapper;
import ru.skillbox.diplom.group40.social.network.impl.repository.post.PostRepository;

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

    Pageable sortedByPostText = PageRequest.of(0, 2, Sort.by("postText"));

    public PostDto save(PostDto postDto) {
        log.info("PostService: save(PostDto postDto) Start method");
        Post post = postMapper.toPost(postDto);
        postRepository.save(post);
        return postMapper.toDto(post);
    }

    public PostDto update(PostDto postDto) {
        log.info("PostService: update(PostDto postDto) Start method");
        postRepository.findById(postDto.getId()).orElseThrow(()
                -> new NotFoundException(notFoundMessage));
        Post currentPost = postMapper.toPost(postDto);
        postRepository.save(currentPost);
        return postMapper.toDto(currentPost);
    }

    public PostDto get(UUID id) {
        log.info("PostService: get(UUID id) Start method");
        return postMapper.toDto(postRepository.findById(id).orElseThrow(()
                -> new NotFoundException(notFoundMessage)));
    }

    public Page<PostDto> getAll() {
        log.info("PostService: getAll() Start method");
        Page<Post> posts = postRepository.findAll(sortedByPostText);
        return posts.map(postMapper::toDto);
    }


    public void deleteById(UUID id) {
        log.info("PostService: deleteById(PostDto postDto) Start method");
        postRepository.findById(id).orElseThrow(() -> new NotFoundException(notFoundMessage));
        postRepository.deleteById(id);
    }

}
