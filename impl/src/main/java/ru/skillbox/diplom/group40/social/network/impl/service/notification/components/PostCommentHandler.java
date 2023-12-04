package ru.skillbox.diplom.group40.social.network.impl.service.notification.components;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.skillbox.diplom.group40.social.network.api.dto.notification.NotificationDTO;
import ru.skillbox.diplom.group40.social.network.api.dto.notification.Type;
import ru.skillbox.diplom.group40.social.network.api.dto.post.PostDto;
import ru.skillbox.diplom.group40.social.network.domain.notification.EventNotification;
import ru.skillbox.diplom.group40.social.network.domain.post.Comment;
import ru.skillbox.diplom.group40.social.network.impl.mapper.notification.NotificationsMapper;
import ru.skillbox.diplom.group40.social.network.impl.service.notification.NotificationHandler;
import ru.skillbox.diplom.group40.social.network.impl.service.notification.NotificationSettingsService;
import ru.skillbox.diplom.group40.social.network.impl.service.post.CommentService;
import ru.skillbox.diplom.group40.social.network.impl.service.post.PostService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class PostCommentHandler implements NotificationHandler{
    private final PostService postService;
    private final CommentService commentService;
    private final NotificationSettingsService notificationSettingsService;
    private final NotificationsMapper notificationsMapper;

    @Override
    public List<EventNotification> getEventNotificationList(NotificationDTO notificationDTO) {
        log.info("PostComment: getPostComment(NotificationDTO notificationDTO) startMethod, " +
                "NotificationDTO : {}", notificationDTO);
        List<EventNotification> listEventNotifications = new ArrayList<>();

        Comment comment = commentService.getByAuthorIdAndTime(notificationDTO.getAuthorId(),
//                notificationDTO.getSentTime().toLocalDateTime());
                notificationDTO.getSentTime());

        PostDto postDto = postService.get(comment.getPostId());
        UUID accountId = postDto.getAuthorId();

        log.info("PostComment: getPostComment(NotificationDTO notificationDTO) получен UUID автора поста: {}",
                accountId);

        if(notificationSettingsService.isNotificationTypeEnables(accountId,notificationDTO.getNotificationType()) &
                !accountId.equals(notificationDTO.getAuthorId())){
            listEventNotifications.add(notificationsMapper.createEventNotification(notificationDTO, accountId));
        }
        log.info("PostCommentHandler: getEventNotificationList(_): Получен List<EventNotification>: {}," +
                "для NotificationDTO : {}", listEventNotifications, notificationDTO);
        return listEventNotifications;
    }

    @Override
    public Type myType() {
        return Type.POST_COMMENT;
    }

}