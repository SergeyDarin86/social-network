package ru.skillbox.diplom.group40.social.network.api.resource.notification;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skillbox.diplom.group40.social.network.api.dto.notification.*;

import java.util.UUID;

@RequestMapping("api/v1/notifications")
public interface NotificationResource {

    @GetMapping("")
    ResponseEntity<NotificationsDTO> getAll();
    @GetMapping("/page")
    ResponseEntity<Page<ContentDTO>> getAllNew(Pageable page);

    @GetMapping("/count")
    ResponseEntity<CountDTO> getCount();

    @PutMapping("/readed")
    ResponseEntity setAllReaded();

    @PostMapping("/add")
    ResponseEntity add(@RequestBody EventNotificationDTO eventNotificationDTO);

    @GetMapping("/settings")
    ResponseEntity<?> getSettings();

    @PutMapping("/settings")
    ResponseEntity setSetting(@RequestBody SettingUpdateDTO settingUpdateDTO);

    @PostMapping("/settings{id}")
    ResponseEntity<Boolean> createSettings(@PathVariable UUID id);

    @PostMapping("/test")
    ResponseEntity<Boolean> test(@RequestBody NotificationDTO notificationDTO);

}
