package ru.skillbox.diplom.group40.social.network.api.dto.notification;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ContentDTO {

    private LocalDateTime timeStamp;
    private NotificationDTO data;

}
