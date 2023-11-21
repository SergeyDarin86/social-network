package ru.skillbox.diplom.group40.social.network.api.dto.post;

import lombok.Data;
import ru.skillbox.diplom.group40.social.network.api.dto.base.BaseDto;

import java.time.ZonedDateTime;
import java.util.UUID;

@Data
public class CommentDto extends BaseDto {
    private CommentType commentType;
    private ZonedDateTime time;
    private ZonedDateTime timeChanged;
    private UUID authorId;
    private UUID parentId;
    private String commentText;
    private UUID postId;
    private Boolean isBlocked;
    private Integer likeAmount;
    private Boolean myLike;
    private Integer commentsCount;
    private String imagePath;
}