package ru.skillbox.diplom.group40.social.network.impl.mapper.post;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.skillbox.diplom.group40.social.network.api.dto.post.PostDto;
import ru.skillbox.diplom.group40.social.network.api.dto.post.Type;
import ru.skillbox.diplom.group40.social.network.api.dto.tag.TagDto;
import ru.skillbox.diplom.group40.social.network.domain.post.Post;
import ru.skillbox.diplom.group40.social.network.impl.mapper.tag.TagMapperImpl;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

//@ExtendWith(SpringExtension.class)
//@ContextConfiguration(classes = {PostMapperImpl.class, TagMapperImpl.class})

//@RunWith(SpringRunner.class)
@SpringBootTest(classes = {PostMapperImpl.class, TagMapperImpl.class})
class PostMapperTest {

//    private PostMapper postMapperImpl = Mappers.getMapper(PostMapper.class);

    @Autowired
    private PostMapper postMapperImpl;

    @Test
    void toPost() {
        TagDto tagDto = new TagDto();
        tagDto.setName("полезное");
        List<TagDto> tagList = new ArrayList<>();
        tagList.add(tagDto);

        PostDto postDto = new PostDto();
        postDto.setPostText("Hello World!!! Today is amazing day");
        postDto.setTitle("Title");
        postDto.setTags(tagList);
        Post post = postMapperImpl.toPost(postDto);

        assertThat(post).isNotNull()
                .hasFieldOrPropertyWithValue("likeAmount", post.getLikeAmount())
                .hasFieldOrPropertyWithValue("commentsCount", post.getCommentsCount())
                .hasFieldOrPropertyWithValue("isBlocked", post.getIsBlocked())
                .hasFieldOrPropertyWithValue("isDeleted", post.getIsDeleted())
                .hasFieldOrPropertyWithValue("type", post.getType())
                .hasFieldOrPropertyWithValue("time", post.getTime())
                .hasFieldOrPropertyWithValue("tags", post.getTags());
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

        PostDto postDto = postMapperImpl.toDto(post);

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
        post = postMapperImpl.toPost(postDto, post);

        assertThat(post)
                .hasFieldOrPropertyWithValue("title", "Foxes")
                .hasFieldOrProperty("timeChanged").isNotNull();
    }

//    default ZonedDateTime setTime(PostDto dto) {
//        return dto.getPublishDate() == null ? ZonedDateTime.now() : dto.getPublishDate();
//    }

    @Test
    void setTime() {
        PostDto dto = new PostDto();
        dto.setPublishDate(ZonedDateTime.now());

        assertThat(dto).hasFieldOrPropertyWithValue("publishDate", postMapperImpl.setTime(dto));
    }

//    default Type setType(PostDto dto) {
//        if (dto.getPublishDate() == null)
//        dto.setPublishDate(ZonedDateTime.now());

//        Type type = dto.getPublishDate().isAfter(ZonedDateTime.now()) ? Type.QUEUED : Type.POSTED;
//        return type;
//    }

    @Test
    void setType() {
        PostDto dto = new PostDto();
        dto.setPublishDate(ZonedDateTime.now().plusDays(1));
        dto.setType(Type.QUEUED);

        assertThat(dto).hasFieldOrPropertyWithValue("type", postMapperImpl.setType(dto));
    }
}