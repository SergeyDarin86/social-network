package ru.skillbox.diplom.group40.social.network.impl.mapper.post;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import ru.skillbox.diplom.group40.social.network.api.dto.post.CommentDto;
import ru.skillbox.diplom.group40.social.network.domain.post.CommentEntity;
import ru.skillbox.diplom.group40.social.network.impl.mapper.base.BaseMapper;

@Component
@Mapper(componentModel = "spring")
public interface CommentMapper extends BaseMapper {
    CommentDto modelToDto(CommentEntity commentEntity);
    CommentEntity dtoToModel(CommentDto commentDto);
}
