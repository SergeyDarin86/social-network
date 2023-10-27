package ru.skillbox.diplom.group40.social.network.impl.repository.post;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import ru.skillbox.diplom.group40.social.network.api.dto.post.LikeType;
import ru.skillbox.diplom.group40.social.network.domain.post.Like;
import ru.skillbox.diplom.group40.social.network.domain.post.Post;
import ru.skillbox.diplom.group40.social.network.impl.repository.base.BaseRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Repository
public interface LikeRepository extends JpaRepository<Like, UUID>, JpaSpecificationExecutor<Like>, BaseRepository<Like> {
    Optional<Like> findFirstByAuthorIdAndItemId(UUID authorId, UUID itemId);
    List<Like> findAllByItemIdAndAndType(UUID itemId, LikeType type);
    List<Like> findAllByItemId(UUID itemId);

    Optional<Like> findByItemId(UUID itemId);

    Optional<Like>  findById(UUID likeId);

}
