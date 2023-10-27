package ru.skillbox.diplom.group40.social.network.api.dto.notification;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CountDTO {

    private LocalDateTime timeStamp;
    private PartCountDTO data;

}