package ru.skilllbox.diplom.group40.social.service.resource;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.web.bind.annotation.RestController;
import ru.skilllbox.diplom.group40.social.service.dto.PersonDto;
import ru.skilllbox.diplom.group40.social.service.service.PersonService;

import java.util.UUID;

/**
 * PersonResourceImpl
 *
 * @author Your name
 */

@Getter
@Setter
@RestController
@RequiredArgsConstructor
public class PersonResourceImpl implements PersonResourse {

    private PersonService personService;

    @Override
    public PersonDto getById(UUID id) {
        return null;
    }
}
