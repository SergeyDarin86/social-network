package ru.skillbox.diplom.group40.social.network.impl.service.dialog;

import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.socket.TextMessage;
import ru.skillbox.diplom.group40.social.network.api.dto.dialog.*;
import ru.skillbox.diplom.group40.social.network.api.dto.search.BaseSearchDto;
import ru.skillbox.diplom.group40.social.network.domain.dialog.Dialog;
import ru.skillbox.diplom.group40.social.network.domain.dialog.Dialog_;
import ru.skillbox.diplom.group40.social.network.domain.dialog.Message;
import ru.skillbox.diplom.group40.social.network.impl.mapper.dialog.DialogMapper;
import ru.skillbox.diplom.group40.social.network.impl.mapper.dialog.MessageMapper;
import ru.skillbox.diplom.group40.social.network.impl.mapper.notification.NotificationsMapper;
import ru.skillbox.diplom.group40.social.network.impl.repository.dialog.DialogRepository;
import ru.skillbox.diplom.group40.social.network.impl.service.kafka.KafkaService;
import ru.skillbox.diplom.group40.social.network.impl.utils.auth.AuthUtil;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import static ru.skillbox.diplom.group40.social.network.impl.utils.specification.SpecificationUtils.equal;
import static ru.skillbox.diplom.group40.social.network.impl.utils.specification.SpecificationUtils.getBaseSpecification;

@Service
@Transactional
@RequiredArgsConstructor
public class DialogService {
    private final KafkaService kafkaService;
    private final MessageMapper messageMapper;
    private final DialogMapper dialogMapper;
    private final MessageService messageService;
    private final DialogRepository dialogRepository;
    private final NotificationsMapper notificationsMapper;

    public Page<DialogDto> getDialogs(Pageable page) {
        BaseSearchDto baseSearchDto = new BaseSearchDto();
        baseSearchDto.setIsDeleted(false);
        Specification<Dialog> dialogSpecification = getBaseSpecification(baseSearchDto)
                .and(equal(Dialog_.CONVERSATION_PARTNER1, AuthUtil.getUserId())
                        .or(equal(Dialog_.CONVERSATION_PARTNER2, AuthUtil.getUserId())));

        Page<Dialog> dialogs = dialogRepository.findAll(dialogSpecification, page);
        List<Dialog> dialogList = dialogs.getContent();
        List<UUID> lastMessageIds = dialogList.stream()
                .map(Dialog::getLastMessage).toList();
        List<Message> lastMessages = messageService.getMessages(lastMessageIds);
        List<MessageDto> messageDtoList = messageMapper.messagesToDto(lastMessages);
        return dialogMapper.mapMessageDtoToDialogDto(dialogs, messageDtoList);
    }

    public DialogDto getDialogByRecipientId(String id) {
        Dialog dialog = getDialogDtoByRecipientId(UUID.fromString(id));
        Message message = messageService.getMessage(dialog.getLastMessage());
        MessageDto messageDto = messageMapper.messageToDto(message);
        return dialogMapper.dialogToDialogDto(dialog, messageDto, id);
    }

    public Page<MessageShortDto> getMessagesByRecipientId(String recipientId, Pageable page) {
        Dialog dialog = getDialogDtoByRecipientId(UUID.fromString(recipientId));
        Page<MessageDto> messageDto = messageService.getMessagesByDialogId(dialog.getId(),page);
        return messageDto.map(messageMapper::messageDtoToMessageShortDto);
    }

    public UnreadCountDto getUnreadDialogsCount() {
        BaseSearchDto baseSearchDto = new BaseSearchDto();
        baseSearchDto.setIsDeleted(false);
        Specification dialogSpecification = getBaseSpecification(baseSearchDto)
                .and(equal(Dialog_.CONVERSATION_PARTNER1, AuthUtil.getUserId())
                        .or(equal(Dialog_.CONVERSATION_PARTNER2, AuthUtil.getUserId())));
        List<Dialog> dialogList = dialogRepository.findAll(dialogSpecification);
        List<Message> lastMessages = dialogList.stream()
                .map(dialog -> messageService.getMessage(dialog.getLastMessage()))
                .filter(Objects::nonNull)
                .toList();
        int sentMessageCount = (int) lastMessages.stream()
                .filter(message -> message.getReadStatus() == ReadStatus.SENT)
                .count();
        return new UnreadCountDto(sentMessageCount);
    }

    public void markDialogRead(String dialogId) {
        messageService.markMessagesRead(dialogId);
    }

    public void handleSocketMessage(TextMessage socketMessage) {
        JSONObject jsonSocketMessage = new JSONObject(socketMessage.getPayload());
        JSONObject jsonMessageDto = (jsonSocketMessage.getJSONObject("data"));
        MessageDto messageDto = messageMapper.getMessageDto(jsonMessageDto);
        Message message = messageMapper.dtoToMessage(messageDto);
        Message persistedMessage = messageService.save(message);
        updateLastMessage(persistedMessage.getDialogId(), persistedMessage.getId());
        createNotification(message);
        System.out.println(socketMessage);
    }

    private void createNotification(Message message) {
        kafkaService.sendNotification(notificationsMapper.getNotificationDTO(message));
    }

    private void updateLastMessage(UUID dialogId, UUID id) {
        Dialog dialog = dialogRepository.getReferenceById(dialogId);
        dialog.setLastMessage(id);
        dialogRepository.save(dialog);
    }

    private Dialog getDialogDtoByRecipientId(UUID uuid){
        BaseSearchDto baseSearchDto = new BaseSearchDto();
        baseSearchDto.setIsDeleted(false);
        Specification dialogSpecification = getBaseSpecification(baseSearchDto)
                .and((equal(Dialog_.CONVERSATION_PARTNER1, AuthUtil.getUserId())
                        .and(equal(Dialog_.CONVERSATION_PARTNER2, uuid))).or(equal(Dialog_.CONVERSATION_PARTNER1, uuid)
                        .and(equal(Dialog_.CONVERSATION_PARTNER2, AuthUtil.getUserId()))));
        List<Dialog> list = dialogRepository.findAll(dialogSpecification);
        return list.isEmpty()? getNewDialog(uuid) : list.get(0);
    }

    private Dialog getNewDialog(UUID uuid) {
        Dialog dialog = new Dialog(0,AuthUtil.getUserId(), uuid, null);
        dialog.setIsDeleted(false);
        return dialogRepository.save(dialog);
    }


}
