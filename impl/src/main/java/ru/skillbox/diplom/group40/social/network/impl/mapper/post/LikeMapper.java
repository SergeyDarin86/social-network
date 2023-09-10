package ru.skillbox.diplom.group40.social.network.impl.mapper.post;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.stereotype.Component;
import ru.skillbox.diplom.group40.social.network.api.dto.post.LikeDto;
import ru.skillbox.diplom.group40.social.network.domain.post.Like;
import ru.skillbox.diplom.group40.social.network.impl.mapper.base.BaseMapper;
@Component
@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface LikeMapper extends BaseMapper {
    LikeDto modelToDto(Like like);
    Like dtoToModel(LikeDto likeDto);
}
