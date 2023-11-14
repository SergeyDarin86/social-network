package ru.skillbox.diplom.group40.social.network.impl.service.notification.components;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.skillbox.diplom.group40.social.network.api.dto.notification.NotificationDTO;
import ru.skillbox.diplom.group40.social.network.api.dto.notification.Type;
import ru.skillbox.diplom.group40.social.network.api.dto.post.CommentDto;
import ru.skillbox.diplom.group40.social.network.api.dto.post.LikeType;
import ru.skillbox.diplom.group40.social.network.api.dto.post.PostDto;
import ru.skillbox.diplom.group40.social.network.domain.notification.EventNotification;
import ru.skillbox.diplom.group40.social.network.domain.post.Like;
import ru.skillbox.diplom.group40.social.network.impl.mapper.notification.NotificationsMapper;
import ru.skillbox.diplom.group40.social.network.impl.service.notification.NotificationHandler;
import ru.skillbox.diplom.group40.social.network.impl.service.notification.NotificationSettingsService;
import ru.skillbox.diplom.group40.social.network.impl.service.post.CommentService;
import ru.skillbox.diplom.group40.social.network.impl.service.post.LikeService;
import ru.skillbox.diplom.group40.social.network.impl.service.post.PostService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class LikeHandler implements NotificationHandler {
    private final LikeService likeService;
    private final CommentService commentService;
    private final PostService postService;
    private final NotificationSettingsService notificationSettingsService;
    private final NotificationsMapper notificationsMapper;
    private static final String SEND_LIKE_POST = "поставил LIKE на Вашу запись \"";
    private static final String SEND_LIKE_COMMENT = "поставил LIKE на Ваш комментарий \"";

    @Override
    public List<EventNotification> getEventNotificationList(NotificationDTO notificationDTO) {
        log.info("LikeHandler: getEventNotificationList(NotificationDTO notificationDTO) startMethod, " +
                "NotificationDTO : {}", notificationDTO);
        List<EventNotification> listEventNotifications = new ArrayList<>();

        UUID likeId = notificationDTO.getAuthorId();
        Like like = likeService.getLike(likeId);
        UUID accountId = null;

        if (like.getType().equals(LikeType.POST)) {
            PostDto postDto = postService.get(like.getItemId());
            accountId = postDto.getAuthorId();

            notificationDTO.setContent(SEND_LIKE_POST.concat(postDto.getPostText().concat("\"")));
        }

        if (like.getType().equals(LikeType.COMMENT)) {
            CommentDto commentDto = commentService.get(like.getItemId());
            accountId = commentDto.getAuthorId();
            log.info("Like: getLike(NotificationDTO notificationDTO) получен UUID автора COMMENT'а: {}," +
                            "для NotificationDTO : {}", accountId, notificationDTO);
            notificationDTO.setContent(SEND_LIKE_COMMENT.concat(commentDto.getCommentText()).concat("\""));
        }

        UUID authorId = like.getAuthorId();
        notificationDTO.setAuthorId(authorId);

        if (notificationSettingsService.isNotificationTypeEnables(accountId, notificationDTO.getNotificationType()) &
                !accountId.equals(notificationDTO.getAuthorId())) {
            listEventNotifications.add(notificationsMapper.createEventNotification(notificationDTO, accountId));
        }
        log.info("CommentCommentHandler: getEventNotificationList(_): Получен List listEventNotifications: {}" +
                        "для NotificationDTO : {}", listEventNotifications, notificationDTO);
        return listEventNotifications;
    }

    @Override
    public Type myType() {
        return Type.LIKE;
    }
}
