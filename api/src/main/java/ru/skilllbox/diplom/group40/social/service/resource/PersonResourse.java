package ru.skilllbox.diplom.group40.social.service.resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.skilllbox.diplom.group40.social.service.dto.PersonDto;

import java.util.UUID;

/**
 * PersonResourse
 *
 * @author Your name
 */
@RequestMapping("api/v1/person/")
public interface PersonResourse {

    @GetMapping(value = "/{id}")
    PersonDto getById(@PathVariable(name = "id") UUID id);
}
