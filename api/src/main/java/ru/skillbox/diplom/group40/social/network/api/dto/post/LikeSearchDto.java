package ru.skillbox.diplom.group40.social.network.api.dto.post;

import lombok.Data;
import ru.skillbox.diplom.group40.social.network.api.dto.base.BaseDto;

import java.time.LocalDateTime;
import java.util.UUID;
@Data
public class LikeSearchDto extends BaseDto {
    private UUID authorId;
    private UUID itemId;
}
