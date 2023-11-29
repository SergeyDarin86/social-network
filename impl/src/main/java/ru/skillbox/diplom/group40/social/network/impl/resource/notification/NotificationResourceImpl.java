package ru.skillbox.diplom.group40.social.network.impl.resource.notification;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skillbox.diplom.group40.social.network.api.dto.notification.*;
import ru.skillbox.diplom.group40.social.network.api.resource.notification.NotificationResource;
import ru.skillbox.diplom.group40.social.network.domain.notification.Settings;
import ru.skillbox.diplom.group40.social.network.impl.service.notification.NotificationService;
import ru.skillbox.diplom.group40.social.network.impl.service.notification.NotificationSettingsService;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class NotificationResourceImpl implements NotificationResource {

    private final NotificationService notificationService;
    private final NotificationSettingsService notificationSettingsService;

    @Override
    public ResponseEntity<NotificationsDTO> getAll() {return ResponseEntity.ok(notificationService.getAll());}

    @Override
    public ResponseEntity<Page<ContentDTO>> getAllNew(Pageable page) {
        return ResponseEntity.ok(notificationService.getAllNew(page));
    }

    @Override
    public ResponseEntity<CountDTO> getCount() {return ResponseEntity.ok(notificationService.getCount());}

    @Override
    public ResponseEntity setAllReaded() {
        notificationService.setAllReaded();
        return ResponseEntity.ok(null);
    }

    @Override
    public ResponseEntity<Settings> getSettings() {
        return ResponseEntity.ok(notificationSettingsService.getSettings());
    }

    @Override
    public ResponseEntity setSetting(SettingUpdateDTO settingUpdateDTO) {
        notificationSettingsService.setSetting(settingUpdateDTO);
        return ResponseEntity.ok(null);
    }

    @Override
    public ResponseEntity<Boolean> createSettings(UUID id) {
        return ResponseEntity.ok(notificationSettingsService.createSettings(id));
    }

    @Override
    public ResponseEntity add(EventNotificationDTO eventNotificationDTO) {
        notificationService.addNotification(eventNotificationDTO);
        return ResponseEntity.ok(null);
    }

    @Override
    public ResponseEntity test(NotificationDTO notificationDTO) {
        notificationService.create(notificationDTO);
        return ResponseEntity.ok(null);
    }


}

