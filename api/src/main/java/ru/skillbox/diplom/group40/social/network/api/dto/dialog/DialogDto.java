package ru.skillbox.diplom.group40.social.network.api.dto.dialog;

import lombok.Getter;
import lombok.Setter;
import ru.skillbox.diplom.group40.social.network.api.dto.base.BaseDto;

import java.util.List;
import java.util.UUID;

@Setter
@Getter
public class DialogDto extends BaseDto {
    Integer unreadCount;
    UUID conversationPartner1;
    UUID conversationPartner2;
    List<MessageDto> lastMessage;
}
