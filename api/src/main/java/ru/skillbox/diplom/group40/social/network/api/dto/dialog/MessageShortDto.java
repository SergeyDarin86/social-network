package ru.skillbox.diplom.group40.social.network.api.dto.dialog;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import ru.skillbox.diplom.group40.social.network.api.dto.base.BaseDto;

import java.time.ZonedDateTime;
import java.util.UUID;

@Setter
@Getter
public class MessageShortDto extends BaseDto {
    @Schema(description = "время отправки сообщения")
    ZonedDateTime time;
    @Schema(description = "uuid отправителя")
    UUID conversationPartner1;
    @Schema(description = "uuid получателя")
    UUID conversationPartner2;
    @Schema(description = "текст сообщения")
    String messageText;
}
