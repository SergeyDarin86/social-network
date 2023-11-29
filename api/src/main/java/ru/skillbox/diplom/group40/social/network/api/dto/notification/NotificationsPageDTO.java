package ru.skillbox.diplom.group40.social.network.api.dto.notification;

import lombok.Data;
import org.springframework.data.domain.Pageable;

@Data
public class NotificationsPageDTO {

    Pageable page;

}
