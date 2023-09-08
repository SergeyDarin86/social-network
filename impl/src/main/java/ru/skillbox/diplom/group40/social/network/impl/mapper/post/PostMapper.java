package ru.skillbox.diplom.group40.social.network.impl.mapper.post;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import ru.skillbox.diplom.group40.social.network.api.dto.post.PostDto;
import ru.skillbox.diplom.group40.social.network.domain.post.Post;
@Component
@Mapper(componentModel = "spring")
public interface PostMapper {
    Post toPost(PostDto dto);
    PostDto toDto(Post post);
}
