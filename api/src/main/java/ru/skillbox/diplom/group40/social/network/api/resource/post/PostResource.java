package ru.skillbox.diplom.group40.social.network.api.resource.post;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skillbox.diplom.group40.social.network.api.dto.post.*;

import javax.security.auth.login.AccountException;
import java.util.UUID;

/**
 * PostResource
 *
 * @author Sergey D.
 */

@RestController
@RequestMapping("/api/v1/post")
public interface PostResource {

    @PostMapping("")
    ResponseEntity create(@RequestBody PostDto postDto);

    @PutMapping("")
    ResponseEntity<PostDto> update(@RequestBody PostDto postDto);

    @GetMapping("/{id}")
    ResponseEntity get(@PathVariable UUID id);

    @GetMapping("/")
    ResponseEntity getAll(PostSearchDto postSearchDto, Pageable page) throws AccountException;

    @DeleteMapping("/{id}")
    ResponseEntity deleteById(@PathVariable UUID id);

    @PutMapping("/{id}/comment")
    public ResponseEntity<CommentDto> updateComment(@RequestBody CommentDto commentDto);
    @PostMapping("/{id}/comment")
    @ResponseBody
    public ResponseEntity<CommentDto> createComment(@RequestBody CommentDto commentDto,
                                                    @PathVariable UUID id);
    @PutMapping("/{id}/comment/{commentId}")
    public ResponseEntity<CommentDto> updateSubComment(@RequestBody CommentDto commentDto, @PathVariable UUID commentId);
    @DeleteMapping("/{id}/comment/{commentId}")
    public ResponseEntity deleteComment(@PathVariable UUID id, @PathVariable UUID commentId);
    @PostMapping("/{id}/comment/{commentId}/like")
    public ResponseEntity<LikeDto> likeComment(@PathVariable UUID id, @PathVariable UUID commentId);
    @DeleteMapping("/{id}/comment/{commentId}/like")
    public ResponseEntity deleteCommentLike(@PathVariable UUID id, @PathVariable UUID commentId);
    @PostMapping("/{id}/like")
    @ResponseBody
    public ResponseEntity<LikeDto> likePost(@PathVariable UUID id,
                                            @RequestBody LikeDto response);
    @DeleteMapping("/{id}/like")
    public ResponseEntity deletePostLike(@PathVariable UUID id);
    @GetMapping("/{postId}/comment")
    ResponseEntity getPostComments(@PathVariable UUID postId,
                                   CommentSearchDto commentSearchDto, Pageable page);
    @GetMapping("/{postId}/comment/{commentId}/subcomment")
    ResponseEntity getSubcomments(@PathVariable UUID postId,
                                  @PathVariable UUID commentId,
                                  CommentSearchDto commentSearchDto, Pageable page);

    @PutMapping("/delayed")
    ResponseEntity delayed();

}
