package ru.skillbox.diplom.group40.social.network.impl.mapper.post;

import org.mapstruct.*;
import org.springframework.stereotype.Component;
import ru.skillbox.diplom.group40.social.network.api.dto.post.PostDto;
import ru.skillbox.diplom.group40.social.network.api.dto.post.Type;
import ru.skillbox.diplom.group40.social.network.domain.post.Post;

import ru.skillbox.diplom.group40.social.network.impl.mapper.base.BaseMapper;

import java.time.LocalDateTime;

@Component
@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface PostMapper extends BaseMapper {

    Post toPost(PostDto dto);

    PostDto toDto(Post post);
    Post toPost(PostDto dto, @MappingTarget Post post);

    default Post toPostForCreate(PostDto postDto){
        postDto.setType(Type.POSTED);
        postDto.setTime(LocalDateTime.now());
        postDto.setIsBlocked(false);
        postDto.setIsDeleted(false);
        postDto.setLikeAmount(0);
        postDto.setMyLike(true);
        postDto.setCommentsCount(0);
        postDto.setMyReaction("");
        postDto.setReactionType("");
        postDto.setPublishDate(LocalDateTime.now());
        return toPost(postDto);
    }

    default Post toPostForUpdate(PostDto dto, Post post){
        post.setTimeChanged(LocalDateTime.now());
        return toPost(dto, post);
    }

}
