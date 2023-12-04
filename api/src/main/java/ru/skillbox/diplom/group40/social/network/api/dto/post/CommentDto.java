package ru.skillbox.diplom.group40.social.network.api.dto.post;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import ru.skillbox.diplom.group40.social.network.api.dto.base.BaseDto;

import java.time.ZonedDateTime;
import java.util.UUID;

@Data
@Schema(description = "DTO для работы с комментариями (создание, редактирование)")
public class CommentDto extends BaseDto {

    @Schema(description = "Тип комментария (к комментарию или к посту)")
    private CommentType commentType;

    @Schema(description = "Время, когда был оставлен комментарий")
    private ZonedDateTime time;

    @Schema(description = "Время изменения комментария")
    private ZonedDateTime timeChanged;

    @Schema(description = "Id автора комментария")
    private UUID authorId;

    @Schema(description = "Id родительского объекта, к которому оставили комментарий (пост или комментарий)")
    private UUID parentId;

    @Schema(description = "Текст комментария")
    private String commentText;

    @Schema(description = "Id поста")
    private UUID postId;

    @Schema(description = "Заблокирован или нет автор", defaultValue = "false")
    private Boolean isBlocked;

    @Schema(description = "Количество лайков", defaultValue = "1")
    private Integer likeAmount;

    @Schema(description = "Есть ли мой лайк к комментарию")
    private Boolean myLike;

    @Schema(description = "Количество комментарием", defaultValue = "2")
    private Integer commentsCount;

    @Schema(description = "Путь к изображению, которое используется в посте." +
            " Формируется автоматически после добавления изображения в пост",
            defaultValue = "https://res.cloudinary.com/net/image/upload/v1700829702/fizdftx3pyrpa36vnhpp.jpg")
    private String imagePath;
}