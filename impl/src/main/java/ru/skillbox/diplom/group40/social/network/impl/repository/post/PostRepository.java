package ru.skillbox.diplom.group40.social.network.impl.repository.post;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import ru.skillbox.diplom.group40.social.network.domain.post.Post;
import ru.skillbox.diplom.group40.social.network.impl.repository.base.BaseRepository;

import java.util.UUID;

/**
 * PostRepository
 *
 * @author Sergey D.
 */

@Repository
public interface PostRepository extends JpaRepository<Post, UUID>, JpaSpecificationExecutor<Post>, BaseRepository<Post> {

}
