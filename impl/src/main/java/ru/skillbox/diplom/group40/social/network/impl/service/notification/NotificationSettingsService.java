package ru.skillbox.diplom.group40.social.network.impl.service.notification;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skillbox.diplom.group40.social.network.api.dto.notification.NotificationDTO;
import ru.skillbox.diplom.group40.social.network.api.dto.notification.SettingUpdateDTO;
import ru.skillbox.diplom.group40.social.network.api.dto.notification.Type;
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

    public Settings getSettings() {
        UUID userId = AuthUtil.getUserId();
        log.info("NotificationSettingsService: getSettings() startMethod, received UUID: {}", userId);
        return settingsRepository.findByAccountId(userId);
    }

    public void setSetting(SettingUpdateDTO settingUpdateDTO) {
        UUID userId = getUserId();
        log.info("NotificationSettingsService: setSetting(SettingUpdateDTO settingUpdateDTO) startMethod, received UUID: {}, " +
                "settingUpdateDTO: {}", userId, settingUpdateDTO);

        Settings notificationSettings = settingsRepository.findByAccountId(userId);

        rewriteSettings(notificationSettings, settingUpdateDTO);

        settingsRepository.save(notificationSettings);

        log.info("NotificationSettingsService: setSetting(SettingUpdateDTO settingUpdateDTO) updated settings: {}, " +
                "NotificationSettings: {}", userId, notificationSettings);

    }

    private UUID getUserId() {
        UUID userId = AuthUtil.getUserId();
        log.info("NotificationSettingsService: getUserId() startMethod, UUID: {}", userId);
        return userId;
    }

    private void rewriteSettings(Settings notificationSettings, SettingUpdateDTO settingUpdateDTO) {

        if (settingUpdateDTO.getNotificationType().equals(Type.LIKE)) {
            notificationSettings.setEnableLike(settingUpdateDTO.isEnable());
        }

        if (settingUpdateDTO.getNotificationType().equals(Type.POST)) {
            notificationSettings.setEnablePost(settingUpdateDTO.isEnable());
        }

        if (settingUpdateDTO.getNotificationType().equals(Type.POST_COMMENT)) {
            notificationSettings.setEnablePostComment(settingUpdateDTO.isEnable());
        }

        if (settingUpdateDTO.getNotificationType().equals(Type.COMMENT_COMMENT)) {
            notificationSettings.setEnableCommentComment(settingUpdateDTO.isEnable());
        }

        if (settingUpdateDTO.getNotificationType().equals(Type.MESSAGE)) {
            notificationSettings.setEnableMessage(settingUpdateDTO.isEnable());
        }

        if (settingUpdateDTO.getNotificationType().equals(Type.FRIEND_REQUEST)) {
            notificationSettings.setEnableFriendRequest(settingUpdateDTO.isEnable());
        }

        if (settingUpdateDTO.getNotificationType().equals(Type.FRIEND_BIRTHDAY)) {
            notificationSettings.setEnableFriendBirthday(settingUpdateDTO.isEnable());
        }

        if (settingUpdateDTO.getNotificationType().equals(Type.SEND_EMAIL_MESSAGE)) {
            notificationSettings.setEnableSendEmailMessage(settingUpdateDTO.isEnable());
        }
    }
}
