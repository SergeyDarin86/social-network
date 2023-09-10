package ru.skillbox.diplom.group40.social.network.impl.repository.base;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.transaction.annotation.Transactional;
import ru.skillbox.diplom.group40.social.network.domain.base.BaseEntity;

import java.util.Optional;
import java.util.UUID;

@NoRepositoryBean
public class BaseRepositoryImpl<Entity extends BaseEntity> extends SimpleJpaRepository<Entity, UUID> implements BaseRepository<Entity> {

    EntityManager entityManager;

    public BaseRepositoryImpl(JpaEntityInformation<Entity, UUID> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
        this.entityManager = entityManager;
    }

    @Transactional
    @Override
    public void deleteById(UUID uuid) {
        this.findById(uuid).ifPresent(entity -> {
            entity.setIsDeleted(true);
            super.save(entity);
        });
    }

    @Transactional
    @Override
    public void deleteAllById(Iterable<? extends UUID> uuids) {
        uuids.forEach(uuid -> this.findById(uuid).ifPresent(entity -> {
            entity.setIsDeleted(true);
            super.save(entity);
        }));
    }

    @Transactional
    public Entity getById(UUID uuid) {
        Optional<Entity> entity = super.findById(uuid);
        if (entity.isEmpty()) {
            throw new EntityNotFoundException();
        };
        return entity.get();
    }

    @Transactional
    @Override
    public void hardDeletedById(UUID uuid) {
        super.deleteById(uuid);
    }


}
