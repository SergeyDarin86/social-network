package ru.skillbox.diplom.group40.social.network.api.resource.post;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skillbox.diplom.group40.social.network.api.dto.post.CommentDto;

@RestController
@RequestMapping("/api/v1/post/{id}/comment")
public interface CommentResource {
    @PutMapping("/")
    public ResponseEntity<CommentDto> update(@RequestBody CommentDto commentDto);
    @PostMapping("/")
    public ResponseEntity<CommentDto> create(@RequestBody CommentDto commentDto);
}
