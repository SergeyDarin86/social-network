package ru.skillbox.diplom.group40.social.network.api.resource.post;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@Tag(name = "Api сервиса постов",
        description = "Сервис для создания, редактирования, планирования, получения, удаления постов. " +
                "Создания, получения, удаления комментариев к постам. Создания, получения, удаления лайков к постам, комментариям")
@RestController
@RequestMapping("/api/v1/post")
public interface PostResource {

    @Operation(description = "Отправка запроса на создание поста")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Метод успешно выполнен.",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = PostDto.class)))
                    }),
            @ApiResponse(
                    responseCode = "400",
                    description = "Не верный запрос.",
                    content = {
                            @Content(schema = @Schema(implementation = void.class))
                    }),
            @ApiResponse(
                    responseCode = "401",
                    description = "Авторизация не пройдена. Для использования метода необходимо авторизоваться.",
                    content = {
                            @Content(schema = @Schema(implementation = void.class))
                    })
    })
    @PostMapping("")
    ResponseEntity create(@RequestBody PostDto postDto);

    @Operation(description = "Отправка запроса на редактирование поста")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Метод успешно выполнен.",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = PostDto.class)))
                    }),
            @ApiResponse(
                    responseCode = "400",
                    description = "Не верный запрос.",
                    content = {
                            @Content(schema = @Schema(implementation = void.class))
                    }),
            @ApiResponse(
                    responseCode = "401",
                    description = "Авторизация не пройдена. Для использования метода необходимо авторизоваться.",
                    content = {
                            @Content(schema = @Schema(implementation = void.class))
                    })
    })
    @PutMapping("")
    ResponseEntity<PostDto> update(@RequestBody PostDto postDto);

    @Operation(description = "Отправка запроса на получение поста по его идентификатору")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Метод успешно выполнен.",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = PostDto.class)))
                    }),
            @ApiResponse(
                    responseCode = "400",
                    description = "Не верный запрос.",
                    content = {
                            @Content(schema = @Schema(implementation = void.class))
                    }),
            @ApiResponse(
                    responseCode = "401",
                    description = "Авторизация не пройдена. Для использования метода необходимо авторизоваться.",
                    content = {
                            @Content(schema = @Schema(implementation = void.class))
                    })
    })
    @GetMapping("/{id}")
    ResponseEntity get(
            @Parameter(description = "Идентификатор поста для запроса на получение") @PathVariable UUID id);

    @Operation(description = "Отправка запроса на получение всех постов")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Метод успешно выполнен.",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = PostDto.class)))
                    }),
            @ApiResponse(
                    responseCode = "400",
                    description = "Не верный запрос.",
                    content = {
                            @Content(schema = @Schema(implementation = void.class))
                    }),
            @ApiResponse(
                    responseCode = "401",
                    description = "Авторизация не пройдена. Для использования метода необходимо авторизоваться.",
                    content = {
                            @Content(schema = @Schema(implementation = void.class))
                    })
    })
    @GetMapping("/")
    ResponseEntity getAll(PostSearchDto postSearchDto, Pageable page) throws AccountException;

    @Operation(description = "Отправка запроса на удаление поста")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Метод успешно выполнен.",
                    content = {
                            @Content(schema = @Schema(implementation = void.class))
                    }),
            @ApiResponse(
                    responseCode = "400",
                    description = "Неверный запрос.",
                    content = {
                            @Content(schema = @Schema(implementation = void.class))
                    }),
            @ApiResponse(
                    responseCode = "401",
                    description = "Авторизация не пройдена. Для использования метода необходимо авторизоваться.",
                    content = {
                            @Content(schema = @Schema(implementation = void.class))
                    })
    })
    @DeleteMapping("/{id}")
    ResponseEntity deleteById(
            @Parameter(description = "Идентификатор поста для запроса на удаление") @PathVariable UUID id);

    @Operation(description = "Отправка запроса на изменение комментария к посту")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Метод успешно выполнен.",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = CommentDto.class)))
                    }),
            @ApiResponse(
                    responseCode = "400",
                    description = "Неверный запрос.",
                    content = {
                            @Content(schema = @Schema(implementation = void.class))
                    }),
            @ApiResponse(
                    responseCode = "401",
                    description = "Авторизация не пройдена. Для использования метода необходимо авторизоваться.",
                    content = {
                            @Content(schema = @Schema(implementation = void.class))
                    })
    })
    @PutMapping("/{id}/comment")
    public ResponseEntity<CommentDto> updateComment(@RequestBody CommentDto commentDto);

    @Operation(description = "Отправка запроса на добавление комментария к посту")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Метод успешно выполнен.",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = CommentDto.class)))
                    }),
            @ApiResponse(
                    responseCode = "400",
                    description = "Неверный запрос.",
                    content = {
                            @Content(schema = @Schema(implementation = void.class))
                    }),
            @ApiResponse(
                    responseCode = "401",
                    description = "Авторизация не пройдена. Для использования метода необходимо авторизоваться.",
                    content = {
                            @Content(schema = @Schema(implementation = void.class))
                    })
    })
    @PostMapping("/{id}/comment")
    @ResponseBody
    public ResponseEntity<CommentDto> createComment(@RequestBody CommentDto commentDto,
                                                    @Parameter(description = "Идентификатор поста для запроса на добавление комментария") @PathVariable UUID id);

    @Operation(description = "Отправка запроса на изменение комментария к посту")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Метод успешно выполнен.",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = CommentDto.class)))
                    }),
            @ApiResponse(
                    responseCode = "400",
                    description = "Неверный запрос.",
                    content = {
                            @Content(schema = @Schema(implementation = void.class))
                    }),
            @ApiResponse(
                    responseCode = "401",
                    description = "Авторизация не пройдена. Для использования метода необходимо авторизоваться.",
                    content = {
                            @Content(schema = @Schema(implementation = void.class))
                    })
    })
    @PutMapping("/{id}/comment/{commentId}")
    public ResponseEntity<CommentDto> updateSubComment(@RequestBody CommentDto commentDto,
                                                       @Parameter(description = "Идентификатор комментария для запроса на изменение") @PathVariable UUID commentId);

    @Operation(description = "Отправка запроса на удаление комментария")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Метод успешно выполнен.",
                    content = {
                            @Content(schema = @Schema(implementation = void.class))
                    }),
            @ApiResponse(
                    responseCode = "400",
                    description = "Неверный запрос.",
                    content = {
                            @Content(schema = @Schema(implementation = void.class))
                    }),
            @ApiResponse(
                    responseCode = "401",
                    description = "Авторизация не пройдена. Для использования метода необходимо авторизоваться.",
                    content = {
                            @Content(schema = @Schema(implementation = void.class))
                    })
    })
    @DeleteMapping("/{id}/comment/{commentId}")
    public ResponseEntity deleteComment(
            @Parameter(description = "Идентификатор поста для запроса на удаление") @PathVariable UUID id,
            @Parameter(description = "Идентификатор комментария для запроса на удаление") @PathVariable UUID commentId);

    @Operation(description = "Отправка запроса на добавление лайка к комментарию")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Метод успешно выполнен.",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = LikeDto.class)))
                    }),
            @ApiResponse(
                    responseCode = "400",
                    description = "Неверный запрос.",
                    content = {
                            @Content(schema = @Schema(implementation = void.class))
                    }),
            @ApiResponse(
                    responseCode = "401",
                    description = "Авторизация не пройдена. Для использования метода необходимо авторизоваться.",
                    content = {
                            @Content(schema = @Schema(implementation = void.class))
                    })
    })
    @PostMapping("/{id}/comment/{commentId}/like")
    public ResponseEntity<LikeDto> likeComment(
            @Parameter(description = "Идентификатор поста") @PathVariable UUID id,
            @Parameter(description = "Идентификатор комментария для запроса на добавление лайка") @PathVariable UUID commentId);

    @Operation(description = "Отправка запроса на удаление лайка к комментарию")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Метод успешно выполнен.",
                    content = {
                            @Content(schema = @Schema(implementation = void.class))
                    }),
            @ApiResponse(
                    responseCode = "400",
                    description = "Неверный запрос.",
                    content = {
                            @Content(schema = @Schema(implementation = void.class))
                    }),
            @ApiResponse(
                    responseCode = "401",
                    description = "Авторизация не пройдена. Для использования метода необходимо авторизоваться.",
                    content = {
                            @Content(schema = @Schema(implementation = void.class))
                    })
    })
    @DeleteMapping("/{id}/comment/{commentId}/like")
    public ResponseEntity deleteCommentLike(
            @Parameter(description = "Идентификатор поста для запроса на удаление лайка") @PathVariable UUID id,
            @Parameter(description = "Идентификатор коммментария для запроса на удаление лайка") @PathVariable UUID commentId);

    @Operation(description = "Отправка запроса на добавление лайка к посту")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Метод успешно выполнен.",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = LikeDto.class)))
                    }),
            @ApiResponse(
                    responseCode = "400",
                    description = "Неверный запрос.",
                    content = {
                            @Content(schema = @Schema(implementation = void.class))
                    }),
            @ApiResponse(
                    responseCode = "401",
                    description = "Авторизация не пройдена. Для использования метода необходимо авторизоваться.",
                    content = {
                            @Content(schema = @Schema(implementation = void.class))
                    })
    })
    @PostMapping("/{id}/like")
    @ResponseBody
    public ResponseEntity<LikeDto> likePost(@Parameter(description = "Идентификатор поста для запроса на добавление лайка") @PathVariable UUID id,
                                            @RequestBody LikeDto response);

    @Operation(description = "Отправка запроса на удаление лайка к посту")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Метод успешно выполнен.",
                    content = {
                            @Content(schema = @Schema(implementation = void.class))
                    }),
            @ApiResponse(
                    responseCode = "400",
                    description = "Неверный запрос.",
                    content = {
                            @Content(schema = @Schema(implementation = void.class))
                    }),
            @ApiResponse(
                    responseCode = "401",
                    description = "Авторизация не пройдена. Для использования метода необходимо авторизоваться.",
                    content = {
                            @Content(schema = @Schema(implementation = void.class))
                    })
    })
    @DeleteMapping("/{id}/like")
    public ResponseEntity deletePostLike(
            @Parameter(description = "Идентификатор поста для запроса на удаление лайка") @PathVariable UUID id);

    @Operation(description = "Отправка запроса на получение комментариев к посту")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Метод успешно выполнен.",
                    content = {
                            @Content(
                                    mediaType = "application/json")
                    }),
            @ApiResponse(
                    responseCode = "400",
                    description = "Неверный запрос.",
                    content = {
                            @Content(schema = @Schema(implementation = void.class))
                    }),
            @ApiResponse(
                    responseCode = "401",
                    description = "Авторизация не пройдена. Для использования метода необходимо авторизоваться.",
                    content = {
                            @Content(schema = @Schema(implementation = void.class))
                    })
    })
    @GetMapping("/{postId}/comment")
    ResponseEntity getPostComments(@Parameter(description = "Идентификатор поста для запроса на получение комментариев") @PathVariable UUID postId,
                                   CommentSearchDto commentSearchDto, Pageable page);

    @Operation(description = "Отправка запроса на получение sub-комментариев (комментарии к комметнариям)")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Метод успешно выполнен.",
                    content = {
                            @Content(
                                    mediaType = "application/json")
                    }),
            @ApiResponse(
                    responseCode = "400",
                    description = "Неверный запрос.",
                    content = {
                            @Content(schema = @Schema(implementation = void.class))
                    }),
            @ApiResponse(
                    responseCode = "401",
                    description = "Авторизация не пройдена. Для использования метода необходимо авторизоваться.",
                    content = {
                            @Content(schema = @Schema(implementation = void.class))
                    })
    })
    @GetMapping("/{postId}/comment/{commentId}/subcomment")
    ResponseEntity getSubcomments(@Parameter(description = "Идентификатор поста") @PathVariable UUID postId,
                                  @Parameter(description = "Идентификатор комментария") @PathVariable UUID commentId,
                                  CommentSearchDto commentSearchDto, Pageable page);

    @Operation(description = "Отправка запроса на публикацию поста в запланированное время")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Метод успешно выполнен.",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = PostDto.class)))
                    }),
            @ApiResponse(
                    responseCode = "400",
                    description = "Неверный запрос.",
                    content = {
                            @Content(schema = @Schema(implementation = void.class))
                    }),
            @ApiResponse(
                    responseCode = "401",
                    description = "Авторизация не пройдена. Для использования метода необходимо авторизоваться.",
                    content = {
                            @Content(schema = @Schema(implementation = void.class))
                    })
    })
    @PutMapping("/delayed")
    ResponseEntity delayed();

}
