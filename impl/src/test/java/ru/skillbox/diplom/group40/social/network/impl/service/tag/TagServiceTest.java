package ru.skillbox.diplom.group40.social.network.impl.service.tag;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.skillbox.diplom.group40.social.network.api.dto.tag.TagDto;
import ru.skillbox.diplom.group40.social.network.api.dto.tag.TagSearchDto;
import ru.skillbox.diplom.group40.social.network.domain.tag.Tag;
import ru.skillbox.diplom.group40.social.network.impl.mapper.tag.TagMapper;
import ru.skillbox.diplom.group40.social.network.impl.repository.post.TagRepository;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class})
class TagServiceTest {

    @Mock
    private TagRepository tagRepository;

    @Mock
    private TagMapper tagMapper;

    @InjectMocks
    private TagService tagService;

    @BeforeEach
    public void setUp() {
        tagService = new TagService(tagRepository, tagMapper);
    }

    @Test
    void getAll() {

        TagSearchDto tagSearchDto = new TagSearchDto();
        tagSearchDto.setName("полезное");

        List<TagDto> expected = new ArrayList<>();
        TagDto tagDto = new TagDto();
        tagDto.setName("полезное");
        expected.add(tagDto);

        List<Tag> tagList = new ArrayList<>();
        Tag tag = new Tag();
        tag.setName("полезное");
        tagList.add(tag);

        when(tagRepository.findAllByName(tagSearchDto.getName())).thenReturn(tagList);
        when(tagMapper.toDto(tag)).thenReturn(tagDto);
        List<TagDto> actual = tagService.getAll(tagSearchDto);

        assertEquals(expected, actual);

    }
}