package ru.skillbox.diplom.group40.social.network.impl.service.notification;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skillbox.diplom.group40.social.network.api.dto.notification.NotificationDTO;
import ru.skillbox.diplom.group40.social.network.domain.notification.Settings;
import ru.skillbox.diplom.group40.social.network.impl.repository.notification.SettingsRepository;
import ru.skillbox.diplom.group40.social.network.impl.utils.auth.AuthUtil;

import java.util.UUID;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class NotificationSettingsService {

    private final SettingsRepository settingsRepository;

    public Boolean createSettings(UUID id) {
        Settings notificationSettings = new Settings();
        notificationSettings.setAccountId(id);
        settingsRepository.save(notificationSettings);
        log.info("NotificationSettingsServiceService: createSettings() create NotificationSettings: {}",
                notificationSettings);
        return true;
    }

    public Settings getSettings(UUID accountId) {
        return settingsRepository.findByAccountId(accountId);
    }

    public Settings getSettings() {
        UUID userId = AuthUtil.getUserId();
        log.info("NotificationSettingsService: getSettings() startMethod, received UUID: {}", userId);
        return settingsRepository.findByAccountId(userId);
    }
}
