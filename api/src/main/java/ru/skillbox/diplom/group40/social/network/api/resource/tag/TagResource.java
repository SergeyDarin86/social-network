package ru.skillbox.diplom.group40.social.network.api.resource.tag;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import ru.skillbox.diplom.group40.social.network.api.dto.tag.TagSearchDto;

/**
 * TagResource
 *
 * @author Sergey D.
 */

public interface TagResource {
    @GetMapping
    ResponseEntity getAll(TagSearchDto tagSearchDto);
}
