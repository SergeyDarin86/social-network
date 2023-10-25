package ru.skillbox.diplom.group40.social.network.impl.repository.notification;

import org.springframework.stereotype.Repository;
import ru.skillbox.diplom.group40.social.network.domain.notification.Settings;
import ru.skillbox.diplom.group40.social.network.impl.repository.base.BaseRepository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface SettingsRepository extends BaseRepository<Settings> {
    Settings findByAccountId(UUID accountId);
}
