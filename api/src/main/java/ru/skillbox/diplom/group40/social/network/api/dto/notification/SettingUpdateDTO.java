package ru.skillbox.diplom.group40.social.network.api.dto.notification;

import lombok.Data;

@Data
public class SettingUpdateDTO {
    private boolean enable;
    private Type notificationType;
}
