package ru.skillbox.diplom.group40.social.network.impl.mapper.base;

import org.mapstruct.Mapper;
import ru.skillbox.diplom.group40.social.network.api.dto.base.BaseDto;
import ru.skillbox.diplom.group40.social.network.domain.base.BaseEntity;

@Mapper
public interface BaseMapper {
    BaseDto modelToDto(BaseEntity baseEntity);
    BaseEntity dtoToModel(BaseDto baseDto);
}
