package ru.skillbox.diplom.group40.social.network.api.dto.dialog;

import lombok.Getter;
import lombok.Setter;
import ru.skillbox.diplom.group40.social.network.api.dto.base.BaseDto;

import java.time.LocalDateTime;
import java.util.UUID;

@Setter
@Getter
public class MessageDto extends BaseDto {
    LocalDateTime time;
    UUID conversationPartner1;
    UUID conversationPartner2;
    String messageText;
    ReadStatus readStatus;
    UUID dialogId;
}
