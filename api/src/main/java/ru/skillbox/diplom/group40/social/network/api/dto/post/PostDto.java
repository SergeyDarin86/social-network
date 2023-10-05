package ru.skillbox.diplom.group40.social.network.api.dto.post;

import lombok.Data;
import ru.skillbox.diplom.group40.social.network.api.dto.base.BaseDto;
import ru.skillbox.diplom.group40.social.network.api.dto.tag.TagDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * PostDto
 *
 * @author Sergey D.
 */

@Data
public class PostDto extends BaseDto{

    private LocalDateTime time;

    private LocalDateTime timeChanged;

    private UUID authorId;

    private String title;

    private Type type;

    private String postText;

    private Boolean isBlocked;

    private Integer commentsCount;

    private String reactionType;

    private String myReaction;

    private Integer likeAmount;

    private Boolean myLike;

    private String imagePath;

    private LocalDateTime publishDate;

    private List<TagDto> tags;

}
