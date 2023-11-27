package ru.skillbox.diplom.group40.social.network.impl.mapper.post;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.skillbox.diplom.group40.social.network.api.dto.post.CommentDto;
import ru.skillbox.diplom.group40.social.network.api.dto.post.CommentType;
import ru.skillbox.diplom.group40.social.network.domain.post.Comment;

import java.time.ZonedDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = {CommentMapperImpl.class})
class CommentMapperTest {

    @Autowired
    private CommentMapper commentMapper;

    @Test
    void modelToDto() {
        Comment comment = new Comment();
        comment.setCommentsCount(1);
        comment.setCommentText("Hello!");
        comment.setCommentType(CommentType.POST);
        comment.setIsBlocked(false);
        comment.setTime(ZonedDateTime.now());
        comment.setAuthorId(UUID.randomUUID());
        comment.setImagePath("some path");
        comment.setLikeAmount(1);
        comment.setIsDeleted(false);
        comment.setMyLike(true);
        comment.setParentId(UUID.randomUUID());
        comment.setPostId(UUID.randomUUID());

        CommentDto commentDto = commentMapper.modelToDto(comment);

        assertThat(commentDto)
                .hasFieldOrPropertyWithValue("commentsCount", 1)
                .hasFieldOrPropertyWithValue("commentText", "Hello!")
                .hasFieldOrPropertyWithValue("commentType", CommentType.POST)
                .hasFieldOrPropertyWithValue("isBlocked", false)
                .hasFieldOrPropertyWithValue("time", comment.getTime())
                .hasFieldOrPropertyWithValue("authorId", comment.getAuthorId())
                .hasFieldOrPropertyWithValue("imagePath", "some path")
                .hasFieldOrPropertyWithValue("likeAmount", 1)
                .hasFieldOrPropertyWithValue("isDeleted", false)
                .hasFieldOrPropertyWithValue("myLike", true)
                .hasFieldOrPropertyWithValue("parentId", comment.getParentId())
                .hasFieldOrPropertyWithValue("postId", comment.getPostId());
    }

    @Test
    void dtoToModel() {
        CommentDto commentDto = new CommentDto();
        commentDto.setCommentText("Hello");
        Comment comment = commentMapper.dtoToModel(commentDto);

        assertThat(comment).hasFieldOrPropertyWithValue("commentText", "Hello");
    }

    @Test
    void testDtoToModel() {
        Comment comment = new Comment();
        comment.setCommentText("Hello");

        CommentDto commentDto = new CommentDto();
        commentDto.setCommentText("Bye");

        comment = commentMapper.dtoToModel(commentDto, comment);
        assertThat(comment).hasFieldOrPropertyWithValue("commentText", "Bye");
    }
}