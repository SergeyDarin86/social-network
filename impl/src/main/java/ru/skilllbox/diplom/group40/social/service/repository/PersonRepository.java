package ru.skilllbox.diplom.group40.social.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skilllbox.diplom.group40.social.service.model.Person;

import java.util.UUID;

/**
 * PersonRepository
 *
 * @author Your name
 */
@Repository
public interface PersonRepository extends JpaRepository<Person, UUID> {
}
