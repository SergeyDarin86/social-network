package ru.skillbox.diplom.group40.social.network.api.resource.notification;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skillbox.diplom.group40.social.network.api.dto.notification.*;

import java.util.UUID;

@Tag(name = "Api сервиса уведомлений",
        description = "Сервис для создания, рассылки и получения по запросу уведомлений фиксированных типов")
@RequestMapping("api/v1/notifications")
public interface NotificationResource {

    @GetMapping("")
    ResponseEntity<NotificationsDTO> getAll();

    @Operation(summary = "Отправка запроса на получение уведомлений",
            description = "Отправка запроса на получение всех непрочитанных уведомлений, " +
                    "в ответ приходит page с объектами ContentDTO")
    @GetMapping("/page")
    ResponseEntity<Page<ContentDTO>> getAllNew(Pageable page);

    @Operation(summary = "Отправка запроса на получение количества уведомлений",
            description = "Отправка запроса на получение количества всех непрочитанных уведомлений")
    @GetMapping("/count")
    ResponseEntity<CountDTO> getCount();

    @Operation(summary = "Отправка запроса на отметку всех уведомлений прочитанными",
            description = "Отправка запроса на отметку всех непрочитанных уведомлений прочитанными")
    @PutMapping("/readed")
    ResponseEntity setAllReaded();

    @Operation(summary = "Отправка запроса на добавление уведомления в базу данных",
            description = "Отправка запроса на добавление нового уведомления в базу данных")
    @PostMapping("/add")
    ResponseEntity add(@RequestBody EventNotificationDTO eventNotificationDTO);

    @Operation(summary = "Отправка запроса на получение настроек уведомлений",
            description = "Отправка запроса на получение настроек уведомлений")
    @GetMapping("/settings")
    ResponseEntity<?> getSettings();

    @Operation(summary = "Отправка запроса на изменение настроек уведомлений",
            description = "Отправка запроса на изменение настроек одного типа уведомлений")
    @PutMapping("/settings")
    ResponseEntity setSetting(@RequestBody SettingUpdateDTO settingUpdateDTO);

    @Operation(summary = "Отправка запроса на создание настроек уведомлений, для id",
            description = "Отправка запроса на создание настроек уведомлений, для пользователя с id")
    @PostMapping("/settings{id}")
    ResponseEntity<Boolean> createSettings(@PathVariable UUID id);

    @Operation(summary = "Отправка запроса на создание уведомления",
            description = "Отправка запроса на создание уведомления")
    @PostMapping("/test")
    ResponseEntity<Boolean> test(@RequestBody NotificationDTO notificationDTO);

}
