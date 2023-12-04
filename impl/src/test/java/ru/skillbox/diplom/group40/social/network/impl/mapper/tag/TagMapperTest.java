package ru.skillbox.diplom.group40.social.network.impl.mapper.tag;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import ru.skillbox.diplom.group40.social.network.api.dto.tag.TagDto;
import ru.skillbox.diplom.group40.social.network.domain.tag.Tag;

import static org.assertj.core.api.Assertions.assertThat;

class TagMapperTest {

    private TagMapper tagMapper = Mappers.getMapper(TagMapper.class);

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