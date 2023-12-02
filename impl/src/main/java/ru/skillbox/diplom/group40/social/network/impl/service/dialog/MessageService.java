package ru.skillbox.diplom.group40.social.network.impl.service.dialog;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skillbox.diplom.group40.social.network.api.dto.dialog.MessageDto;
import ru.skillbox.diplom.group40.social.network.api.dto.search.BaseSearchDto;
import ru.skillbox.diplom.group40.social.network.domain.dialog.Message;
import ru.skillbox.diplom.group40.social.network.domain.dialog.Message_;
import ru.skillbox.diplom.group40.social.network.impl.mapper.dialog.MessageMapper;
import ru.skillbox.diplom.group40.social.network.impl.repository.dialog.MessageRepository;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

import static ru.skillbox.diplom.group40.social.network.impl.utils.specification.SpecificationUtils.equal;
import static ru.skillbox.diplom.group40.social.network.impl.utils.specification.SpecificationUtils.getBaseSpecification;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MessageService {
    private final MessageRepository messageRepository;
    private final MessageMapper messageMapper;
    public List<Message> getMessages(List<UUID> lastMessageIds) {
        return messageRepository.findAllById(lastMessageIds);
    }

    public Message getMessage(UUID lastMessage) {
        if (lastMessage==null){
            return null;
        }
        return messageRepository.getReferenceById(lastMessage);
    }

    public Page<MessageDto> getMessagesByDialogId(UUID id, Pageable page) {
        BaseSearchDto baseSearchDto = new BaseSearchDto();
        baseSearchDto.setIsDeleted(false);
        Specification messageSpecification = getBaseSpecification(baseSearchDto)
                .and(equal(Message_.DIALOG_ID, id));
        Page<Message> messages = messageRepository.findAll(messageSpecification, page);
        return messages.map(messageMapper::messageToDto);
    }

    public Message save(Message message) {
        messageRepository.save(message);
        return message;
    }

    public void markMessagesRead(String dialogId) {
        messageRepository.updateSentMessagesToRead(UUID.fromString(dialogId));
    }

    public Timestamp getLastTimestamp() {
        log.info("MessageService: getLastTimestamp() startMethod");
        Timestamp lastTimestamp = messageRepository.findTopDate().orElse(new Timestamp(System.currentTimeMillis()));
        log.info("MessageService: getLastTimestamp() получен LastTime Timestamp: {}", lastTimestamp);
        return lastTimestamp;
    }
}
