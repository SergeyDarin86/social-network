package ru.skillbox.diplom.group40.social.network.impl.mapper.dialog;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;
import ru.skillbox.diplom.group40.social.network.api.dto.dialog.DialogDto;
import ru.skillbox.diplom.group40.social.network.api.dto.dialog.MessageDto;
import ru.skillbox.diplom.group40.social.network.domain.dialog.Dialog;
import ru.skillbox.diplom.group40.social.network.impl.mapper.base.BaseMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface DialogMapper extends BaseMapper {

    @Mapping(source = "messageDtoList", target = "lastMessage")
    DialogDto mapMessageDtoToDialogDto(Dialog dialog, List<MessageDto> messageDtoList);

    default Page<DialogDto> mapMessageDtoToDialogDto(Page<Dialog> dialogs, List<MessageDto> messageDtoList) {
        return dialogs.map(dialog -> mapMessageDtoToDialogDto(dialog, getMatchingMessageDto(messageDtoList,dialog.getLastMessage())));
    }

    private List<MessageDto> getMatchingMessageDto(List<MessageDto> messageDtoList, UUID id){
        for(MessageDto messageDto : messageDtoList){
            if (messageDto.getId().equals(id)){
                return List.of(messageDto);
            }
        }
        return List.of();
    }

    default DialogDto dialogToDialogDto(Dialog dialog, MessageDto messageDto, String recipientId){
        if (messageDto==null){
            return mapMessageDtoToDialogDto(dialog, new ArrayList<>());
        }
        return mapMessageDtoToDialogDto(dialog, List.of(messageDto));
    }
}
