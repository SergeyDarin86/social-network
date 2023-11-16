package ru.skillbox.diplom.group40.social.network.api.dto.dialog;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.skillbox.diplom.group40.social.network.api.dto.base.BaseDto;

import java.time.ZonedDateTime;
import java.util.UUID;

@Setter
@Getter
@ToString
public class MessageDto extends BaseDto {
    ZonedDateTime time;
    UUID conversationPartner1;
    UUID conversationPartner2;
    String messageText;
    ReadStatus readStatus;
    UUID dialogId;
}
