package ru.skillbox.diplom.group40.social.network.impl.service.tag;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skillbox.diplom.group40.social.network.api.dto.tag.TagDto;
import ru.skillbox.diplom.group40.social.network.api.dto.tag.TagSearchDto;
import ru.skillbox.diplom.group40.social.network.api.dto.search.BaseSearchDto;
import ru.skillbox.diplom.group40.social.network.domain.tag.Tag;
import ru.skillbox.diplom.group40.social.network.domain.tag.Tag_;
import ru.skillbox.diplom.group40.social.network.impl.mapper.tag.TagMapper;
import ru.skillbox.diplom.group40.social.network.impl.repository.post.TagRepository;
import ru.skillbox.diplom.group40.social.network.impl.utils.specification.SpecificationUtils;

import java.util.List;

/**
 * TagService
 *
 * @author Sergey D.
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class TagService {

    private final TagRepository repository;
    private final TagMapper mapper;

    public List<TagDto> getAll(TagSearchDto tagSearchDto) {
        log.info("TagService: getAll() Start method " + tagSearchDto);
        BaseSearchDto baseSearchDto = new BaseSearchDto();
        baseSearchDto.setIsDeleted(tagSearchDto.getIsDeleted());
        Specification postDtoSpecification = SpecificationUtils.getBaseSpecification(baseSearchDto)
                .and(SpecificationUtils.like(Tag_.NAME, tagSearchDto.getName()));

        List<Tag> tags = repository.findAll(postDtoSpecification);

        return tags.stream().map(mapper::toDto).toList();
    }

}
