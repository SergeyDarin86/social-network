package ru.skillbox.diplom.group40.social.network.api.resource.post;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skillbox.diplom.group40.social.network.api.dto.post.PostDto;
import ru.skillbox.diplom.group40.social.network.api.dto.post.PostSearchDto;


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
    ResponseEntity getAll(@RequestBody PostSearchDto postSearchDto);

    @DeleteMapping("/{id}")
    ResponseEntity deleteById(@PathVariable UUID id);

}
