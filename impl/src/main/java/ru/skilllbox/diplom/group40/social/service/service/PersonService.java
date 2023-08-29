package ru.skilllbox.diplom.group40.social.service.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;
import ru.skilllbox.diplom.group40.social.service.dto.PersonDto;
import ru.skilllbox.diplom.group40.social.service.model.Person;
import ru.skilllbox.diplom.group40.social.service.repository.PersonRepository;

import java.util.UUID;

/**
 * PersonService
 *
 * @author Your name
 */

@Setter
@Getter
@Service
@RequiredArgsConstructor
public class PersonService {

    private PersonRepository repository;

    public PersonDto getById(UUID id){
        Person person = repository.getReferenceById(id);
        return new PersonDto();
    }
}
