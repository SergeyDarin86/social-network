package ru.skillbox.diplom.group40.social.network.api.dto.post;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import ru.skillbox.diplom.group40.social.network.api.dto.base.BaseDto;

import java.time.LocalDateTime;
import java.util.UUID;
@Data
@Schema(description = "DTO для поиска информации по лайкам")
public class LikeSearchDto extends BaseDto {

    @Schema(description = "Id автора лайка")
    private UUID authorId;

    @Schema(description = "Id объекта, к которому поставлен лайк (пост или комментарий)")
    private UUID itemId;
}
