package ru.skilllbox.diplom.group40.social.network.impl.mapper.base;

import org.mapstruct.Mapper;
import ru.skilllbox.diplom.group40.social.network.api.dto.base.BaseDto;
import ru.skilllbox.diplom.group40.social.network.domain.base.BaseEntity;

@Mapper
public interface BaseMapper {
    BaseDto modelToDto(BaseEntity baseEntity);
    BaseEntity dtoToModel(BaseDto baseDto);
}
