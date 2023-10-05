package ru.skillbox.diplom.group40.social.network.impl.repository.post;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.skillbox.diplom.group40.social.network.domain.tag.Tag;

import java.util.Optional;
import java.util.UUID;

/**
 * TagRepository
 *
 * @author Sergey D.
 */

public interface TagRepository extends JpaRepository<Tag, UUID>, JpaSpecificationExecutor<Tag> {
    Optional<Tag>findByName(String name);
}
