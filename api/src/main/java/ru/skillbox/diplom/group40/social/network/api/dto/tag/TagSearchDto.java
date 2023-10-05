package ru.skillbox.diplom.group40.social.network.api.dto.tag;

import lombok.Data;
import ru.skillbox.diplom.group40.social.network.api.dto.base.BaseDto;

/**
 * TagSearchDto
 *
 * @author Sergey D.
 */

@Data
public class TagSearchDto extends BaseDto {
    private String name;
}
