package ru.skillbox.diplom.group40.social.network.api.dto.dialog;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import ru.skillbox.diplom.group40.social.network.api.dto.base.BaseDto;

import java.util.List;
import java.util.UUID;

@Setter
@Getter
public class DialogDto extends BaseDto {
    @Schema(description = "количество непрочитанных сообщений")
    Integer unreadCount;
    @Schema(description = "uuid одного собеседника")
    UUID conversationPartner1;
    @Schema(description = "uuid одного собеседника")
    UUID conversationPartner2;
    @Schema(description = "последнее сообщение в диалоге")
    List<MessageDto> lastMessage;
}
