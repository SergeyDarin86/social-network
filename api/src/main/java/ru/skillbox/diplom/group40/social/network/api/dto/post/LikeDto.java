package ru.skillbox.diplom.group40.social.network.api.dto.post;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import ru.skillbox.diplom.group40.social.network.api.dto.base.BaseDto;

import java.time.ZonedDateTime;
import java.util.UUID;

@Data
@Schema(description = "DTO для работы с лайками")
public class LikeDto extends BaseDto {
    @Schema(description = "Id автора лайка")
    private UUID authorId;

    @Schema(description = "Время, когда был поставлен лайк")
    private ZonedDateTime time;

    @Schema(description = "Id объекта, к которому поставлен лайк (пост или комментарий)")
    private UUID itemId;

    @Schema(description = "Тип лайка (к комментарию или к посту)")
    private LikeType type;

    @Schema(description = "Тип реакции лайка", example = "heart")
    private String reactionType;
}
