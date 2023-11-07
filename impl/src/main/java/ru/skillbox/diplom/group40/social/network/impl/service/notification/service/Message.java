package ru.skillbox.diplom.group40.social.network.impl.service.notification.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skillbox.diplom.group40.social.network.api.dto.notification.NotificationDTO;
import ru.skillbox.diplom.group40.social.network.impl.service.dialog.MessageService;
import ru.skillbox.diplom.group40.social.network.impl.service.post.CommentService;
import ru.skillbox.diplom.group40.social.network.impl.service.post.LikeService;
import ru.skillbox.diplom.group40.social.network.impl.service.post.PostService;
import ru.skillbox.diplom.group40.social.network.impl.utils.SpringUtils;

import java.util.UUID;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class Message {
    private MessageService messageService;

    public UUID getMessage(NotificationDTO notificationDTO) {

        /** Внедрение зависимостей из контекста */
        messageService = (MessageService) SpringUtils.ctx.getBean(MessageService.class);
        /** */

        log.info("Message: sendMessage(NotificationDTO notificationDTO) startMethod, notificationDTO: {}",
                notificationDTO);

        UUID messageId = notificationDTO.getAuthorId();
        ru.skillbox.diplom.group40.social.network.domain.dialog.Message message = messageService.getMessage(messageId);

        UUID authorId = message.getConversationPartner1();
        UUID accountId = message.getConversationPartner2();
        notificationDTO.setContent(notificationDTO.getContent());
        notificationDTO.setAuthorId(authorId);

//        socketSendOneUser(notificationDTO, accountId);
        return accountId;
    }
}
