package ru.skillbox.diplom.group40.social.network.impl.repository.post;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import ru.skillbox.diplom.group40.social.network.domain.tag.Tag;

import java.util.List;
import java.util.UUID;

/**
 * TagRepository
 *
 * @author Sergey D.
 */

public interface TagRepository extends JpaRepository<Tag, UUID>, JpaSpecificationExecutor<Tag> {
    @Transactional
    @Query(value = "select tag.id, tag.name, tag.is_deleted, count(*) count from tag " +
            " join skillbox.post_tag pt on tag.id = pt.tag_id\n" +
            " join skillbox.post p on p.id = pt.post_id" +
            " where tag.name like ?1% and p.is_deleted = false" +
            " group by tag.id\n" +
            " order by count desc limit 5", nativeQuery = true)
    List<Tag> findAllByName(String name);
}
