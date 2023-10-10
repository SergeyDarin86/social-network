package ru.skillbox.diplom.group40.social.network.api.resource.notification;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skillbox.diplom.group40.social.network.api.dto.notification.CountDTO;
import ru.skillbox.diplom.group40.social.network.api.dto.notification.EventNotificationDTO;
import ru.skillbox.diplom.group40.social.network.api.dto.notification.NotificationsDTO;
import ru.skillbox.diplom.group40.social.network.api.dto.notification.SettingUpdateDTO;

import java.util.UUID;

//@RequestMapping("api/v1/notifications")
public interface NotificationResource {

    @GetMapping("")
    ResponseEntity<NotificationsDTO> getAll();

    @GetMapping("/count")
    ResponseEntity<CountDTO> getCount();

    @PutMapping("/readed")
    ResponseEntity setAllReaded();  // TODO Указать тип ResponseEntity - тут body null

    @PostMapping("/add")
    ResponseEntity add(@RequestBody EventNotificationDTO eventNotificationDTO);   // TODO Указать тип ResponseEntity - тут body null

    @GetMapping("/settings")
    ResponseEntity<?> getSettings();

    @PutMapping("/settings")
    ResponseEntity setSetting(@RequestBody SettingUpdateDTO settingUpdateDTO);

    @PostMapping("/settings{id}")
    ResponseEntity<Boolean> createSettings(@PathVariable UUID id);

}
