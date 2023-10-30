package ru.skillbox.diplom.group40.social.network.impl.mapper.tag;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.stereotype.Component;
import ru.skillbox.diplom.group40.social.network.api.dto.tag.TagDto;
import ru.skillbox.diplom.group40.social.network.domain.tag.Tag;

/**
 * TagMapper
 *
 * @author Sergey D.
 */
@Component
@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface TagMapper {

    TagDto toDto(Tag tag);
    @Mapping(target = "isDeleted", source = "isDeleted", defaultValue = "false")
    Tag toTag(TagDto dto);

}
