package ru.skillbox.diplom.group40.social.network.api.dto.notification;

import lombok.Data;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Data
public class ContentDTO {

    private ZonedDateTime timeStamp;
    private NotificationDTO data;

}
