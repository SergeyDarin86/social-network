package ru.skillbox.diplom.group40.social.network.api.dto.geo;

import lombok.Data;
import ru.skillbox.diplom.group40.social.network.api.dto.base.BaseDto;

import java.util.List;

@Data
public class CountryDto extends BaseDto{
    private String title;
    private List<CityDto> cities;

}
