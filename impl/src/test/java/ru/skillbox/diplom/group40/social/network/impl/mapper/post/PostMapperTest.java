package ru.skillbox.diplom.group40.social.network.impl.mapper.post;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.skillbox.diplom.group40.social.network.api.dto.post.PostDto;
import ru.skillbox.diplom.group40.social.network.api.dto.post.Type;
import ru.skillbox.diplom.group40.social.network.domain.post.Post;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.ZonedDateTime;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class PostMapperTest {

    private PostMapper postMapper = Mappers.getMapper(PostMapper.class);

    @Test
    void toPost() {
        PostDto postDto = new PostDto();
        postDto.setPostText("Hello World!!! Today is amazing day");
        postDto.setTitle("Title");
        Post post = postMapper.toPost(postDto);

        assertThat(post).isNotNull()
                .hasFieldOrPropertyWithValue("likeAmount", post.getLikeAmount())
                .hasFieldOrPropertyWithValue("commentsCount", post.getCommentsCount())
                .hasFieldOrPropertyWithValue("isBlocked", post.getIsBlocked())
                .hasFieldOrPropertyWithValue("isDeleted", post.getIsDeleted())
                .hasFieldOrPropertyWithValue("type", post.getType())
                .hasFieldOrPropertyWithValue("time", post.getTime());
    }

    @Test
    void toDto() {
        Post post = new Post();
        post.setTitle("Planned");
        post.setPostText("Hello World");
        post.setType(Type.POSTED);
        post.setAuthorId(UUID.randomUUID());
        post.setTime(ZonedDateTime.now());
        post.setPublishDate(ZonedDateTime.now());
        post.setCommentsCount(2);
        post.setImagePath("some path");
        post.setLikeAmount(1);
        post.setIsDeleted(false);
        post.setIsBlocked(false);
        post.setTimeChanged(ZonedDateTime.now());

        PostDto postDto = postMapper.toDto(post);

        assertThat(postDto)
                .hasFieldOrPropertyWithValue("title", post.getTitle())
                .hasFieldOrPropertyWithValue("postText", post.getPostText())
                .hasFieldOrPropertyWithValue("type", post.getType())
                .hasFieldOrPropertyWithValue("authorId", post.getAuthorId())
                .hasFieldOrPropertyWithValue("time", post.getTime())
                .hasFieldOrPropertyWithValue("publishDate", post.getPublishDate())
                .hasFieldOrPropertyWithValue("commentsCount", post.getCommentsCount())
                .hasFieldOrPropertyWithValue("imagePath", post.getImagePath())
                .hasFieldOrPropertyWithValue("likeAmount", post.getLikeAmount())
                .hasFieldOrPropertyWithValue("isDeleted", post.getIsDeleted())
                .hasFieldOrPropertyWithValue("isBlocked", post.getIsBlocked())
                .hasFieldOrPropertyWithValue("timeChanged", post.getTimeChanged());
    }

    @Test
    void testToPost() {
        Post post = new Post();
        post.setTitle("Animals");
        post.setPostText("Animals live in the forest");
        PostDto postDto = new PostDto();
        postDto.setTitle("Foxes");
        post = postMapper.toPost(postDto, post);

        assertThat(post)
                .hasFieldOrPropertyWithValue("title", "Foxes")
                .hasFieldOrProperty("timeChanged").isNotNull();
    }

    @Test
    void setTime() {
        PostDto dto = new PostDto();
        dto.setPublishDate(ZonedDateTime.now());

        assertThat(dto).hasFieldOrPropertyWithValue("publishDate", postMapper.setTime(dto));
    }

    @Test
    void setType() {
        PostDto dto = new PostDto();
        dto.setPublishDate(ZonedDateTime.now().plusDays(1));
        dto.setType(Type.QUEUED);

        assertThat(dto).hasFieldOrPropertyWithValue("type", postMapper.setType(dto));
    }
}