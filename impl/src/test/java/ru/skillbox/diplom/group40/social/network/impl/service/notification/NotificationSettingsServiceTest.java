package ru.skillbox.diplom.group40.social.network.impl.service.notification;

import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.skillbox.diplom.group40.social.network.api.dto.notification.SettingUpdateDTO;
import ru.skillbox.diplom.group40.social.network.api.dto.notification.Type;
import ru.skillbox.diplom.group40.social.network.domain.notification.Settings;
import ru.skillbox.diplom.group40.social.network.impl.repository.notification.SettingsRepository;
import ru.skillbox.diplom.group40.social.network.impl.utils.auth.AuthUtil;

import java.util.UUID;

import static org.mockito.Mockito.when;

@Slf4j
@ExtendWith({MockitoExtension.class})
public class NotificationSettingsServiceTest {
    @Mock
    SettingsRepository settingsRepository;
    @Mock
    AuthUtil authUtil;
    @InjectMocks
    private NotificationSettingsService notificationSettingsService;

    @Before
    public void setUp() {
        notificationSettingsService = new NotificationSettingsService(
                settingsRepository);
    }

    @Test
    @DisplayName("Update settings")
    void updateSettings() {

        UUID randomUUID = UUID.randomUUID();

        SettingUpdateDTO settingUpdateDTO = new SettingUpdateDTO();
        settingUpdateDTO.setNotificationType(Type.POST);
        settingUpdateDTO.setEnable(false);

        Settings settings = new Settings();
        settings.setAccountId(randomUUID);

        when(settingsRepository.findByAccountId(randomUUID)).thenReturn(settings);
        when(authUtil.getUserId()).thenReturn(randomUUID);
        when(settingsRepository.save(settings)).thenReturn(settings);

        notificationSettingsService.setSetting(settingUpdateDTO);

    }

}
