package ru.skillbox.diplom.group40.social.network.impl.resource.post;

import lombok.RequiredArgsConstructor;
import org.modelmapper.spi.ErrorMessage;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skillbox.diplom.group40.social.network.api.dto.post.PostDto;
import ru.skillbox.diplom.group40.social.network.api.dto.post.PostSearchDto;
import ru.skillbox.diplom.group40.social.network.api.resource.post.PostResource;
import ru.skillbox.diplom.group40.social.network.impl.exception.NotFoundException;
import ru.skillbox.diplom.group40.social.network.impl.service.post.PostService;

import java.util.UUID;

/**
 * PostResourceImpl
 *
 * @author Sergey D.
 */
@RestController
@RequestMapping("/api/v1/post")
@RequiredArgsConstructor

public class PostResourceImpl implements PostResource {

    private final PostService postService;

    @Override
    @PostMapping("")
    public ResponseEntity create(@RequestBody PostDto postDto) {
        return ResponseEntity.ok(postService.create(postDto));
    }

    @Override
    @PutMapping("")
    public ResponseEntity update(PostDto postDto) {
        return ResponseEntity.ofNullable(postService.update(postDto));
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity get(@PathVariable UUID id) {
        return ResponseEntity.ofNullable(postService.get(id));
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorMessage> handleException(NotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorMessage(exception.getMessage()));
    }

    @Override
    @GetMapping("")
    public ResponseEntity getAll(PostSearchDto postSearchDto, Pageable page) {
        return ResponseEntity.ok(postService.getAll(postSearchDto, page));
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity deleteById(UUID id) {
        postService.deleteById(id);
        return ResponseEntity.ok().body("Пользователь удалён успешно");
    }


}
