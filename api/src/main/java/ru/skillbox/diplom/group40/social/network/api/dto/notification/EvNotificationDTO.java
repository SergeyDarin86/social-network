package ru.skillbox.diplom.group40.social.network.api.dto.notification;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.UUID;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class EvNotificationDTO {

    @Schema(description = "id пользователя, создавшего событие уведомления (пост, запрос на дружбу и т.д.)")
    private UUID authorId;
    @Schema(description = "id пользователя, для которого отправляется уведомление")
    private UUID receiverId;
    @Schema(description = "Тип нотификации")
    private String notificationType;
    @Schema(description = "Сообщение")
    private String content;

}

