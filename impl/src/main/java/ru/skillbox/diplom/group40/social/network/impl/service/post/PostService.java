package ru.skillbox.diplom.group40.social.network.impl.service.post;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skillbox.diplom.group40.social.network.api.dto.post.PostDto;
import ru.skillbox.diplom.group40.social.network.api.dto.post.PostSearchDto;
import ru.skillbox.diplom.group40.social.network.api.dto.search.BaseSearchDto;
import ru.skillbox.diplom.group40.social.network.domain.post.Post;
import ru.skillbox.diplom.group40.social.network.domain.post.Post_;
import ru.skillbox.diplom.group40.social.network.impl.exception.NotFoundException;
import ru.skillbox.diplom.group40.social.network.impl.mapper.post.PostMapper;
import ru.skillbox.diplom.group40.social.network.impl.repository.post.PostRepository;
import ru.skillbox.diplom.group40.social.network.impl.utils.auth.AuthUtil;
import ru.skillbox.diplom.group40.social.network.impl.utils.specification.SpecificationUtils;

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
        log.info("PostService: save(PostDto postDto), title = " + postDto.getTitle() + " (Start method");
        postDto.setAuthorId(AuthUtil.getUserId());

        return postMapper.toDto(postRepository.save(postMapper.toPostForCreate(postDto)));
    }

    public PostDto update(PostDto postDto) {
        log.info("PostService: update(PostDto postDto), id = " + postDto.getId() + " (Start method)");
        Post currentPost = postRepository.findById(postDto.getId()).orElseThrow(()
                -> new NotFoundException(notFoundMessage));

        return postMapper.toDto(postRepository.save(postMapper.toPostForUpdate(postDto, currentPost)));
    }

    public PostDto get(UUID id) {
        log.info("PostService: get(UUID id), id = " + id + " (Start method)");
        return postMapper.toDto(postRepository.findById(id).orElseThrow(()
                -> new NotFoundException(notFoundMessage)));
    }

    public Page<PostDto> getAll(PostSearchDto postSearchDto, Pageable page) {
        log.info("PostService: getAll() Start method " + postSearchDto);

        BaseSearchDto baseSearchDto = new BaseSearchDto();
        baseSearchDto.setIsDeleted(postSearchDto.getIsDeleted());

        Specification postDtoSpecification = SpecificationUtils.getBaseSpecification(baseSearchDto)
                .and(SpecificationUtils.equalIn(Post_.AUTHOR_ID, postSearchDto.getAccountIds()))
                .and(SpecificationUtils.equalIn(Post_.ID, postSearchDto.getIds()))
                .and(SpecificationUtils.like(Post_.POST_TEXT, postSearchDto.getText()))
                .and(SpecificationUtils.betweenDate(Post_.PUBLISH_DATE, postSearchDto.getDateFrom(), postSearchDto.getDateTo()));
        Page<Post> posts = postRepository.findAll(postDtoSpecification, page);

        return posts.map(postMapper::toDto);
    }

    public void deleteById(UUID id) {
        log.info("PostService: deleteById(PostDto postDto), id = " + id + " (Start method)");
        postRepository.findById(id).orElseThrow(() -> new NotFoundException(notFoundMessage));
        postRepository.deleteById(id);
    }

}
