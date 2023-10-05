package ru.skillbox.diplom.group40.social.network.api.resource.post;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skillbox.diplom.group40.social.network.api.dto.post.PostDto;
import ru.skillbox.diplom.group40.social.network.api.dto.post.PostSearchDto;

import javax.security.auth.login.AccountException;
import java.util.UUID;

/**
 * PostResource
 *
 * @author Sergey D.
 */


public interface PostResource {

    @PostMapping("/")
    ResponseEntity create(@RequestBody PostDto postDto);

    @PutMapping("/")
    ResponseEntity<PostDto> update(@RequestBody PostDto postDto);

    @GetMapping("/{id}")
    ResponseEntity get(@PathVariable UUID id);

    @GetMapping("/")
    ResponseEntity getAll(PostSearchDto postSearchDto, Pageable page) throws AccountException;

    @DeleteMapping("/{id}")
    ResponseEntity deleteById(@PathVariable UUID id);

}
