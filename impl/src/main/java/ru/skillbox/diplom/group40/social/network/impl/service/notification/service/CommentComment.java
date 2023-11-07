package ru.skillbox.diplom.group40.social.network.impl.service.notification.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skillbox.diplom.group40.social.network.api.dto.notification.NotificationDTO;
import ru.skillbox.diplom.group40.social.network.api.dto.post.CommentDto;
import ru.skillbox.diplom.group40.social.network.domain.post.Comment;
import ru.skillbox.diplom.group40.social.network.impl.service.post.CommentService;
import ru.skillbox.diplom.group40.social.network.impl.service.post.PostService;
import ru.skillbox.diplom.group40.social.network.impl.utils.SpringUtils;

import java.util.UUID;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CommentComment {
    private CommentService commentService;


    public  UUID getCommentComment(NotificationDTO notificationDTO) {

        /** Внедрение зависимостей из контекста */
        commentService = (CommentService) SpringUtils.ctx.getBean(CommentService.class);
        /** */

        log.info("NotificationService: sendCommentComment(NotificationDTO notificationDTO) startMethod");

        Comment comment = commentService.getByAuthorIdAndTime(notificationDTO.getAuthorId(), notificationDTO.getSentTime().toLocalDateTime());
        CommentDto commentParent = commentService.get(comment.getParentId());
        UUID accountId = commentParent.getAuthorId();
        log.info("NotificationService: sendCommentComment(NotificationDTO notificationDTO) получен UUID автора поста: {}",
                accountId);

//        socketSendOneUser(notificationDTO, accountId);
        return accountId;
    }
}
