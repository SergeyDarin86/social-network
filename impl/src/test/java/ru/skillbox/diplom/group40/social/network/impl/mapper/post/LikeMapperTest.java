package ru.skillbox.diplom.group40.social.network.impl.mapper.post;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.skillbox.diplom.group40.social.network.api.dto.post.LikeDto;
import ru.skillbox.diplom.group40.social.network.api.dto.post.LikeType;
import ru.skillbox.diplom.group40.social.network.domain.post.Like;

import java.time.ZonedDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {LikeMapperImpl.class})
class LikeMapperTest {

    @Autowired
    private LikeMapper likeMapper;

    @Test
    void modelToDto() {
        Like like = new Like();
        like.setType(LikeType.COMMENT);
        like.setTime(ZonedDateTime.now());
        like.setAuthorId(UUID.randomUUID());
        like.setIsDeleted(false);
        like.setItemId(UUID.randomUUID());
        like.setReactionType("heart");

        assertThat(likeMapper.modelToDto(like))
                .hasFieldOrPropertyWithValue("type", LikeType.COMMENT)
                .hasFieldOrPropertyWithValue("time", like.getTime())
                .hasFieldOrPropertyWithValue("authorId", like.getAuthorId())
                .hasFieldOrPropertyWithValue("isDeleted", false)
                .hasFieldOrPropertyWithValue("itemId", like.getItemId())
                .hasFieldOrPropertyWithValue("reactionType", "heart");
    }

    @Test
    void dtoToModel() {
        LikeDto likeDto = new LikeDto();
        likeDto.setAuthorId(UUID.randomUUID());
        likeDto.setReactionType("wow");
        likeDto.setType(LikeType.COMMENT);
        likeDto.setItemId(UUID.randomUUID());
        likeDto.setIsDeleted(false);
        likeDto.setTime(ZonedDateTime.now());

        assertThat(likeMapper.dtoToModel(likeDto))
                .hasFieldOrPropertyWithValue("authorId", likeDto.getAuthorId())
                .hasFieldOrPropertyWithValue("reactionType", "wow")
                .hasFieldOrPropertyWithValue("type", LikeType.COMMENT)
                .hasFieldOrPropertyWithValue("itemId", likeDto.getItemId())
                .hasFieldOrPropertyWithValue("isDeleted", false)
                .hasFieldOrPropertyWithValue("time", likeDto.getTime());
    }
}