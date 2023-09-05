package ru.skillbox.diplom.group40.social.network.impl.mapper.post;

import org.mapstruct.Mapper;
import ru.skillbox.diplom.group40.social.network.api.dto.post.CommentDto;
import ru.skillbox.diplom.group40.social.network.domain.post.CommentEntity;
import ru.skillbox.diplom.group40.social.network.impl.mapper.base.BaseMapper;

@Mapper
public interface CommentMapper extends BaseMapper {
    CommentDto modelToDto(CommentEntity commentEntity);
    CommentEntity dtoToModel(CommentDto commentDto);
}
