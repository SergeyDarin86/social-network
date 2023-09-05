package ru.skillbox.diplom.group40.social.network.impl.resource.post;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skillbox.diplom.group40.social.network.api.dto.post.CommentDto;
import ru.skillbox.diplom.group40.social.network.api.resource.post.CommentResource;
import ru.skillbox.diplom.group40.social.network.impl.service.post.CommentServices;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/post/{id}/comment")
public class CommentResourceImpl implements CommentResource {
    private final CommentServices commentServices;
    @Override
    @PutMapping("/")
    public ResponseEntity<CommentDto> update(CommentDto commentDto){
        return ResponseEntity.ok(commentServices.update(commentDto));
    }
    @Override
    @PostMapping("/")
    public ResponseEntity<CommentDto> create(CommentDto commentDto){
        return ResponseEntity.ok(commentServices.save(commentDto));
    }

}
