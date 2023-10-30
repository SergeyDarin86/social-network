package ru.skillbox.diplom.group40.social.network.impl.repository.post;

import org.springframework.stereotype.Repository;
import ru.skillbox.diplom.group40.social.network.api.dto.post.Type;
import ru.skillbox.diplom.group40.social.network.domain.post.Post;
import ru.skillbox.diplom.group40.social.network.impl.repository.base.BaseRepository;

import java.time.ZonedDateTime;
import java.util.List;

/**
 * PostRepository
 *
 * @author Sergey D.
 */

@Repository
public interface PostRepository extends BaseRepository<Post> {
List<Post> findAllByTypeAndPublishDateBefore(Type type, ZonedDateTime time);
}
