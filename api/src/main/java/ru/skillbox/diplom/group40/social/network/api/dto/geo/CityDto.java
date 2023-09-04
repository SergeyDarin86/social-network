package ru.skillbox.diplom.group40.social.network.api.dto.geo;

import lombok.Data;
import ru.skillbox.diplom.group40.social.network.api.dto.base.BaseDto;

import java.util.UUID;

@Data
public class CityDto extends BaseDto {
    private String title;
    private UUID countryId;
}
