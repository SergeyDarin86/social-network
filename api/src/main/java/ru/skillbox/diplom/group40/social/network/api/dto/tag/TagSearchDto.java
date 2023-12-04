package ru.skillbox.diplom.group40.social.network.api.dto.tag;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import ru.skillbox.diplom.group40.social.network.api.dto.base.BaseDto;

/**
 * TagSearchDto
 *
 * @author Sergey D.
 */

@Data
@Schema(description = "DTO для поиска информации по тегам")
public class TagSearchDto extends BaseDto {

    @Schema(description = "Название тега", example = "#полезное")
    private String name;
}
