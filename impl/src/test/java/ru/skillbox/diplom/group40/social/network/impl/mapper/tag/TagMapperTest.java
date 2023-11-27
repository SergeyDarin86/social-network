package ru.skillbox.diplom.group40.social.network.impl.mapper.tag;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.skillbox.diplom.group40.social.network.api.dto.tag.TagDto;
import ru.skillbox.diplom.group40.social.network.domain.tag.Tag;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = {TagMapperImpl.class})
class TagMapperTest {

    @Autowired
    private TagMapper tagMapper;

    @Test
    void toDto() {
        Tag tag = new Tag();
        tag.setName("полезное");
        tag.setIsDeleted(false);

        TagDto tagDto = tagMapper.toDto(tag);
        assertThat(tagDto)
                .hasFieldOrProperty("name").isNotNull()
                .hasFieldOrPropertyWithValue("isDeleted", false);
    }

    @Test
    void toTag() {
        TagDto tagDto = new TagDto();
        tagDto.setName("полезное");

        Tag tag = tagMapper.toTag(tagDto);
        assertThat(tag).hasFieldOrPropertyWithValue("isDeleted", false);
    }
}