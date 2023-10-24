package ru.skillbox.diplom.group40.social.network.impl.mapper.post;

import org.mapstruct.*;
import org.springframework.stereotype.Component;
import ru.skillbox.diplom.group40.social.network.api.dto.post.PostDto;
import ru.skillbox.diplom.group40.social.network.api.dto.post.Type;
import ru.skillbox.diplom.group40.social.network.domain.post.Post;

import ru.skillbox.diplom.group40.social.network.impl.mapper.base.BaseMapper;
import ru.skillbox.diplom.group40.social.network.impl.mapper.tag.TagMapper;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * PostMapper
 *
 * @author Sergey D.
 */
@Component
@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        imports = LocalDateTime.class, uses = {TagMapper.class})
public interface PostMapper extends BaseMapper {

    @Mappings({
            @Mapping(target = "likeAmount", source = "likeAmount", defaultValue = "0"),
            @Mapping(target = "commentsCount", source = "commentsCount", defaultValue = "0"),
            @Mapping(target = "isBlocked", source = "isBlocked", defaultValue = "false"),
            @Mapping(target = "isDeleted", source = "isDeleted", defaultValue = "false"),
            @Mapping(target = "type", source = "type", defaultExpression = "java(setType(dto))"),
            @Mapping(target = "time", source = "time", defaultExpression = "java(LocalDateTime.now())"),
            @Mapping(target = "tags", source = "tags")
//            @Mapping(target = "publishDate", source = "publishDate", defaultExpression = "java(LocalDateTime.now())")
    })
    Post toPost(PostDto dto);

    @Mapping(target = "tags", source = "tags")
    PostDto toDto(Post post);

    @Mapping(target = "timeChanged", source = "timeChanged", defaultExpression = "java(LocalDateTime.now())")
    Post toPost(PostDto dto, @MappingTarget Post post);

//    default Type setType(PostDto dto) {
//        if (dto.getPublishDate() == null) dto.setPublishDate(LocalDateTime.now());
//        Type type = dto.getPublishDate().isAfter(LocalDateTime.now()) ? Type.QUEUED : Type.POSTED;
//        return type;
//    }

    default Type setType(PostDto dto) {

        if (dto.getPublishDate() == null){
            LocalDateTime localDateTime = LocalDateTime.now();
            dto.setPublishDate(localDateTime);
        }else {
            Instant instant = dto.getPublishDate().toInstant(ZoneOffset.UTC);
            LocalDateTime ldt = LocalDateTime.ofInstant(instant, ZoneOffset.systemDefault());
            dto.setPublishDate(ldt);
        }

        Type type = dto.getPublishDate().isAfter(LocalDateTime.now()) ? Type.QUEUED : Type.POSTED;
        return type;
    }

}
