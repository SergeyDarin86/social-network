package ru.skillbox.diplom.group40.social.network.impl.resource.post;

import lombok.RequiredArgsConstructor;
import org.modelmapper.spi.ErrorMessage;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skillbox.diplom.group40.social.network.api.dto.post.*;
import ru.skillbox.diplom.group40.social.network.api.resource.post.PostResource;
import ru.skillbox.diplom.group40.social.network.impl.exception.NotFoundException;
import ru.skillbox.diplom.group40.social.network.impl.service.post.CommentService;
import ru.skillbox.diplom.group40.social.network.impl.service.post.PostService;

import javax.security.auth.login.AccountException;
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
    private final CommentService commentService;

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
    public ResponseEntity getAll(PostSearchDto postSearchDto, Pageable page) throws AccountException {
        return ResponseEntity.ok(postService.getAll(postSearchDto, page));
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity deleteById(UUID id) {
        postService.deleteById(id);
        return ResponseEntity.ok().body("Пользователь удалён успешно");
    }
    @Override
    @PutMapping("/{id}/comment")
    public ResponseEntity<CommentDto> updateComment(@RequestBody CommentDto commentDto){
        return ResponseEntity.ok(commentService.update(commentDto));
    }
    @Override
    @PostMapping("/{id}/comment")
    public ResponseEntity<CommentDto> createComment(@RequestBody CommentDto commentDto,
                                                    @PathVariable UUID id){
        return ResponseEntity.ok(postService.createComment(commentDto, id));
    }
    @Override
    @PutMapping("/{id}/comment/{commentId}")
    public ResponseEntity<CommentDto> updateSubComment(@RequestBody CommentDto commentDto, @PathVariable UUID commentId){
        return ResponseEntity.ok(commentService.updateSub(commentDto, commentId));
    }
    @DeleteMapping("/{id}/comment/{commentId}")
    public ResponseEntity deleteComment(@PathVariable UUID id, @PathVariable UUID commentId) {
        postService.deleteComment(id, commentId);
        return ResponseEntity.ok().body("Comment deleted");
    }

    @Override
    @PostMapping("/{id}/comment/{commentId}/like")
    public ResponseEntity<LikeDto> likeComment(@PathVariable UUID id, @PathVariable UUID commentId) {
        return ResponseEntity.ok(commentService.createLikeForComment(id, commentId));
    }

    @Override
    @DeleteMapping("/{id}/comment/{commentId}/like")
    public ResponseEntity deleteCommentLike(@PathVariable UUID id, @PathVariable UUID commentId) {
        commentService.deleteLikeForComment(id, commentId);
        return ResponseEntity.ok().body("Like canceled");
    }

    @Override
    @PostMapping("/{id}/like")
    public ResponseEntity<LikeDto> likePost(@PathVariable UUID id,
                                            @RequestBody LikeDto response) {
        return ResponseEntity.ok(postService.createLikeForPost(id, response));
    }

    @Override
    @DeleteMapping("/{id}/like")
    public ResponseEntity deletePostLike(@PathVariable UUID id) {
        postService.deleteLikeForPost(id);
        return ResponseEntity.ok().body("Like canceled");
    }

    @Override
    @GetMapping("/{postId}/comment")
    public ResponseEntity getPostComments(@PathVariable UUID postId,
                                          CommentSearchDto commentSearchDto, Pageable page) {
        return ResponseEntity.ok(postService.getPostComments(postId, commentSearchDto, page));
    }
    @Override
    @GetMapping("/{postId}/comment/{commentId}/subcomment")
    public ResponseEntity getSubcomments(@PathVariable UUID postId,
                                         @PathVariable UUID commentId,
                                         CommentSearchDto commentSearchDto, Pageable page) {
        return ResponseEntity.ok(postService.getSubcomments(postId, commentId, commentSearchDto, page));
    }

    @Override
    @PutMapping("/delayed")
    public ResponseEntity delayed() {
        postService.delayed();
        return ResponseEntity.ok().build();
    }

}
