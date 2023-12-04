package ru.skillbox.diplom.group40.social.network.api.dto.post;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import ru.skillbox.diplom.group40.social.network.api.dto.base.BaseDto;
import ru.skillbox.diplom.group40.social.network.api.dto.tag.TagDto;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

/**
 * PostDto
 *
 * @author Sergey D.
 */

@Data
@Schema(description = "DTO для работы с постами (создание, редактирование)")
public class PostDto extends BaseDto {

    @Schema(description = "Время публикации поста")
    private ZonedDateTime time;

    @Schema(description = "Время изменения поста")
    private ZonedDateTime timeChanged;

    @Schema(description = "Id автора поста")
    private UUID authorId;

    @Schema(description = "Заголовок поста", defaultValue = "Про животных")
    private String title;

    @Schema(description = "Тип поста (опубликован или запланирован)")
    private Type type;

    @Schema(description = "Текст поста, его содержимое", defaultValue = "Содержимое поста")
    private String postText;

    @Schema(description = "Заблокирован ли автор поста", defaultValue = "false")
    private Boolean isBlocked;

    @Schema(description = "Количество комментариев к посту", defaultValue = "2")
    private Integer commentsCount;

    @Schema(description = "Список реакций лайков")
    private List<LikeReaction> reactionType;

    @Schema(description = "Моя реакция")
    private String myReaction;

    @Schema(description = "Количество лайков к посту", defaultValue = "1")
    private Integer likeAmount;

    @Schema(description = "Есть ли мой лайк к посту")
    private Boolean myLike;

    @Schema(description = "Путь к изображению, которое используется в посте." +
            " Формируется автоматически после добавления изображения в пост",
            defaultValue = "https://res.cloudinary.com/net/image/upload/v1700829702/fizdftx3pyrpa36vnhpp.jpg")
    private String imagePath;

    @Schema(description = "Дата публикации поста")
    private ZonedDateTime publishDate;

    @Schema(description = "Список тегов к посту")
    private List<TagDto> tags;

}
