package ru.skillbox.diplom.group40.social.network.api.dto.post;

import lombok.Data;
import ru.skillbox.diplom.group40.social.network.api.dto.base.BaseDto;

import java.time.ZonedDateTime;
import java.util.UUID;

@Data
public class LikeDto extends BaseDto {
    private UUID authorId;
    private ZonedDateTime time;
    private UUID itemId;
    private LikeType type;
    private String reactionType;
}
