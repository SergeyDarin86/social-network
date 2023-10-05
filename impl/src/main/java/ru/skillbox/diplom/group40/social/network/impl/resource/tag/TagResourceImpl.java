package ru.skillbox.diplom.group40.social.network.impl.resource.tag;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.skillbox.diplom.group40.social.network.api.dto.tag.TagSearchDto;
import ru.skillbox.diplom.group40.social.network.api.resource.tag.TagResource;
import ru.skillbox.diplom.group40.social.network.impl.service.tag.TagService;

/**
 * TagResourceImpl
 *
 * @author Sergey D.
 */

@RestController
@RequestMapping("/api/v1/tag")
@RequiredArgsConstructor
public class TagResourceImpl implements TagResource {

    private final TagService tagService;

    @Override
    @GetMapping
    public ResponseEntity getAll(TagSearchDto tagSearchDto) {
        return ResponseEntity.ok(tagService.getAll(tagSearchDto));
    }
}
