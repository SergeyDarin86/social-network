package ru.skillbox.diplom.group40.social.network.impl.resource.notification;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skillbox.diplom.group40.social.network.api.dto.notification.*;
import ru.skillbox.diplom.group40.social.network.api.resource.notification.NotificationResource;
import ru.skillbox.diplom.group40.social.network.domain.notification.EventNotification;
import ru.skillbox.diplom.group40.social.network.domain.notification.Settings;
import ru.skillbox.diplom.group40.social.network.impl.service.notification.NotificationService;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/notifications")
public class NotificationResourceImpl implements NotificationResource {

    private final NotificationService notificationService;

    @Override
    public ResponseEntity<NotificationsDTO> getAll() {return ResponseEntity.ok(notificationService.getAll());}

    @Override
    public ResponseEntity<CountDTO> getCount() {return ResponseEntity.ok(notificationService.getCount());}

    @Override
    public ResponseEntity setAllReaded() {
        notificationService.setAllReaded();
        return ResponseEntity.ok(null);
    }

    @Override
    public ResponseEntity<Settings> getSettings() {
        return ResponseEntity.ok(notificationService.getSettings());
    }

    @Override
    public ResponseEntity setSetting(SettingUpdateDTO settingUpdateDTO) {
        notificationService.setSetting(settingUpdateDTO);
        return ResponseEntity.ok(null);
    }

    @Override
    public ResponseEntity<Boolean> createSettings(UUID id) {
        return ResponseEntity.ok(notificationService.createSettings(id));
    }

    @Override
    public ResponseEntity add(EventNotificationDTO eventNotificationDTO) {   // TODO Проставить возвращаемую ResponseEntity
        notificationService.addNotification(eventNotificationDTO);
        return ResponseEntity.ok(null);
    }

}

