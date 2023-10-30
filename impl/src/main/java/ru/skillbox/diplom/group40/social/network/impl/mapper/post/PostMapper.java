package ru.skillbox.diplom.group40.social.network.impl.mapper.post;

import org.mapstruct.*;
import org.springframework.stereotype.Component;
import ru.skillbox.diplom.group40.social.network.api.dto.post.PostDto;
import ru.skillbox.diplom.group40.social.network.api.dto.post.Type;
import ru.skillbox.diplom.group40.social.network.domain.post.Post;

import ru.skillbox.diplom.group40.social.network.impl.mapper.base.BaseMapper;
import ru.skillbox.diplom.group40.social.network.impl.mapper.tag.TagMapper;

import java.time.ZonedDateTime;

/**
 * PostMapper
 *
 * @author Sergey D.
 */
@Component
@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        imports = ZonedDateTime.class, uses = {TagMapper.class})
public interface PostMapper extends BaseMapper {

    @Mappings({
            @Mapping(target = "likeAmount", source = "likeAmount", defaultValue = "0"),
            @Mapping(target = "commentsCount", source = "commentsCount", defaultValue = "0"),
            @Mapping(target = "isBlocked", source = "isBlocked", defaultValue = "false"),
            @Mapping(target = "isDeleted", source = "isDeleted", defaultValue = "false"),
            @Mapping(target = "type", source = "type", defaultExpression = "java(setType(dto))"),
            @Mapping(target = "time", source = "time", defaultExpression = "java(setTime(dto))"),
            @Mapping(target = "tags", source = "tags")
    })
    Post toPost(PostDto dto);

    @Mapping(target = "tags", source = "tags")
    PostDto toDto(Post post);

    @Mapping(target = "timeChanged", source = "timeChanged", defaultExpression = "java(ZonedDateTime.now())")
    Post toPost(PostDto dto, @MappingTarget Post post);

    default ZonedDateTime setTime(PostDto dto) {
        return dto.getPublishDate() == null ? ZonedDateTime.now() : dto.getPublishDate();
    }

    default Type setType(PostDto dto) {
        if (dto.getPublishDate() == null) dto.setPublishDate(ZonedDateTime.now());
        Type type = dto.getPublishDate().isAfter(ZonedDateTime.now()) ? Type.QUEUED : Type.POSTED;
        return type;
    }

}
