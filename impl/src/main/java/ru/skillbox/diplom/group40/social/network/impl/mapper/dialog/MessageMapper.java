package ru.skillbox.diplom.group40.social.network.impl.mapper.dialog;

import org.json.JSONObject;
import org.mapstruct.Mapper;
import ru.skillbox.diplom.group40.social.network.api.dto.dialog.MessageDto;
import ru.skillbox.diplom.group40.social.network.api.dto.dialog.MessageShortDto;
import ru.skillbox.diplom.group40.social.network.api.dto.dialog.ReadStatus;
import ru.skillbox.diplom.group40.social.network.domain.dialog.Message;
import ru.skillbox.diplom.group40.social.network.impl.mapper.base.BaseMapper;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface MessageMapper extends BaseMapper {
    List<MessageDto> messagesToDto(List<Message> messageEntity);

    MessageDto messageToDto(Message messageEntity);

    Message dtoToMessage(MessageDto messageDto);

    MessageShortDto messageDtoToMessageShortDto(MessageDto messageDto);

    default MessageDto getMessageDto(JSONObject jsonMessageDto){
        MessageDto messageDto = new MessageDto();
        messageDto.setTime(ZonedDateTime.now());
        messageDto.setConversationPartner1(UUID.fromString(jsonMessageDto.getString("conversationPartner1")));
        messageDto.setConversationPartner2(UUID.fromString(jsonMessageDto.getString("conversationPartner2")));
        messageDto.setMessageText(jsonMessageDto.getString("messageText"));
        messageDto.setDialogId(UUID.fromString(jsonMessageDto.getString("dialogId")));
        messageDto.setReadStatus(ReadStatus.SENT);
        messageDto.setIsDeleted(false);
        return messageDto;
    }
}
