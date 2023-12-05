package ru.skillbox.diplom.group40.social.network.api.dto.notification;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.skillbox.diplom.group40.social.network.api.dto.base.BaseDto;

import java.time.ZonedDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationDTO extends BaseDto {

    @Schema(description = "id пользователя, создавшего событие уведомления (пост, запрос на дружбу и т.д.)")
    UUID authorId;
    @Schema(description = "id пользователя, для которого отправляется уведомление")
    UUID receiverId;
    @Schema(description = "Сообщение")
    String content;
    @Schema(description = "Тип нотификации")
    Type notificationType;
    @Schema(description = "Время события (поста, запроса на дружбу и т.д.), создавшего уведомление")
    ZonedDateTime sentTime;

}
