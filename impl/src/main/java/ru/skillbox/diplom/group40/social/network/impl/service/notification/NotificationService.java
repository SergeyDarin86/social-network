package ru.skillbox.diplom.group40.social.network.impl.service.notification;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import ru.skillbox.diplom.group40.social.network.api.dto.notification.*;
import ru.skillbox.diplom.group40.social.network.api.dto.post.CommentDto;
import ru.skillbox.diplom.group40.social.network.api.dto.post.LikeType;
import ru.skillbox.diplom.group40.social.network.api.dto.post.PostDto;
import ru.skillbox.diplom.group40.social.network.domain.dialog.Message;
import ru.skillbox.diplom.group40.social.network.domain.notification.EventNotification;
import ru.skillbox.diplom.group40.social.network.domain.notification.EventNotification_;
import ru.skillbox.diplom.group40.social.network.domain.notification.Settings;
import ru.skillbox.diplom.group40.social.network.domain.post.Comment;
import ru.skillbox.diplom.group40.social.network.domain.post.Like;
import ru.skillbox.diplom.group40.social.network.domain.post.Post;
import ru.skillbox.diplom.group40.social.network.impl.exception.NotFoundException;
import ru.skillbox.diplom.group40.social.network.impl.mapper.notification.NotificationMapper;
import ru.skillbox.diplom.group40.social.network.impl.mapper.notification.NotificationsMapper;
import ru.skillbox.diplom.group40.social.network.impl.repository.notification.EventNotificationRepository;
import ru.skillbox.diplom.group40.social.network.impl.repository.notification.SettingsRepository;
import ru.skillbox.diplom.group40.social.network.impl.repository.post.PostRepository;
import ru.skillbox.diplom.group40.social.network.impl.service.dialog.MessageService;
import ru.skillbox.diplom.group40.social.network.impl.service.friend.FriendService;
import ru.skillbox.diplom.group40.social.network.impl.service.kafka.KafkaService;
import ru.skillbox.diplom.group40.social.network.impl.service.post.CommentService;
import ru.skillbox.diplom.group40.social.network.impl.service.post.LikeService;
import ru.skillbox.diplom.group40.social.network.impl.service.post.PostService;
import ru.skillbox.diplom.group40.social.network.impl.utils.auth.AuthUtil;
import ru.skillbox.diplom.group40.social.network.impl.utils.specification.SpecificationUtils;
import ru.skillbox.diplom.group40.social.network.impl.utils.websocket.WebSocketHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class NotificationService {
    private final EventNotificationRepository eventNotificationRepository;
    private final NotificationSettingsService notificationSettingsService;
    private final FriendService friendService;
    private final LikeService likeService;
    private final CommentService commentService;
    private final MessageService messageService;
    private final PostService postService;
    private final KafkaService kafkaService;
    private final WebSocketHandler webSocketHandler;
    private final NotificationsMapper notificationsMapper;
    private final NotificationMapper notificationMapper;
    private static final String SEND_LIKE_POST = "поставил LIKE на Вашу запись \"";
    private static final String SEND_LIKE_COMMENT = "поставил LIKE на Ваш комментарий \"";
    private static final String SEND_EMAIL_MESSAGE = "Вам на почту отправлена ссылка для восстановления пароля";
    private static final String SEND_FRIEND_REQUEST_MESSAGE2 = "хочет добавить Вас в друзья";

    public void create(NotificationDTO notificationDTO) {
        log.info("NotificationService: create(NotificationDTO notificationDTO) startMethod, notificationDTO: {}",
                notificationDTO);

        switch (notificationDTO.getNotificationType()) {
            case LIKE:
                sendLike(notificationDTO);
                break;
            case POST:
                sendAllFriend(notificationDTO);
                break;
            case POST_COMMENT:
                sendPostComment(notificationDTO);
                break;
            case COMMENT_COMMENT:
                sendCommentComment(notificationDTO);
                break;
            case MESSAGE:
                sendMessage(notificationDTO);
                break;
            case FRIEND_REQUEST:
                sendMeFriendRequest(notificationDTO);
                break;
            case FRIEND_BIRTHDAY:
                notificationDTO.setContent("");
                sendAllFriend(notificationDTO);
                break;
            case SEND_EMAIL_MESSAGE:
                sendEmail(notificationDTO);
                break;

        }

    }

    public void socketSendOneUser(NotificationDTO notificationDTO, UUID accountId) {
        if(notificationSettingsService.isNotificationTypeEnables(accountId,notificationDTO.getNotificationType())){
            eventNotificationRepository.save(notificationsMapper
                    .createEventNotification(notificationDTO, accountId));

            kafkaService.sendSocketNotificationDTO(notificationsMapper
                    .getSocketNotificationDTO(notificationDTO, accountId));
        }
    }


    public void sendLike(NotificationDTO notificationDTO) {
        log.info("NotificationService: sendLike(NotificationDTO notificationDTO) startMethod, NotificationDTO = {}",
                notificationDTO);
        UUID likeId = notificationDTO.getAuthorId();
        Like like = likeService.getLike(likeId);
        UUID accountId = null;

        if (like.getType().equals(LikeType.POST)) {
            PostDto postDto = postService.get(like.getItemId());
            accountId = postDto.getAuthorId();
            log.info("NotificationService: sendLike(NotificationDTO notificationDTO) получен UUID автора POST'а: {}",
                    accountId);
            notificationDTO.setContent(SEND_LIKE_POST.concat(postDto.getPostText().concat("\"")));
        }

        if (like.getType().equals(LikeType.COMMENT)) {
            CommentDto commentDto = commentService.get(like.getItemId());
            accountId = commentDto.getAuthorId();
            log.info("NotificationService: sendLike(NotificationDTO notificationDTO) получен UUID автора COMMENT'а: {}",
                    accountId);
            notificationDTO.setContent(SEND_LIKE_COMMENT.concat(commentDto.getCommentText()).concat("\""));
        }

        UUID authorId = like.getAuthorId();
        notificationDTO.setAuthorId(authorId);

        if (!accountId.equals(notificationDTO.getAuthorId())) {
        socketSendOneUser(notificationDTO, accountId);
        }
    }

    public void sendAllFriend(NotificationDTO notificationDTO) {
        log.info("NotificationService: sendAllFriend(NotificationDTO notificationDTO) startMethod");
        List<UUID> allFriends = notificationsMapper.getListUUID(friendService.getAllFriendsById(notificationDTO.getAuthorId()));
        log.info("NotificationService: sendAllFriend(_): Add List<UUID> allFriends: {}",
                allFriends);
        for(UUID accountId : allFriends) {
            socketSendOneUser(notificationDTO, accountId);
        }
    }

    public void sendMeFriendRequest(NotificationDTO notificationDTO) {
        log.info("NotificationService: sendMeFriendRequest(NotificationDTO notificationDTO) startMethod, notificationDTO: {}",
                notificationDTO);
        UUID accountId = UUID.fromString(notificationDTO.getContent());
        notificationDTO.setContent(SEND_FRIEND_REQUEST_MESSAGE2);

        socketSendOneUser(notificationDTO, accountId);
    }

    public void sendMessage(NotificationDTO notificationDTO) {
        log.info("NotificationService: sendMessage(NotificationDTO notificationDTO) startMethod, notificationDTO: {}",
                notificationDTO);

        UUID messageId = notificationDTO.getAuthorId();
        Message message = messageService.getMessage(messageId);

        UUID authorId = message.getConversationPartner1();
        UUID accountId = message.getConversationPartner2();
        notificationDTO.setContent(notificationDTO.getContent());
        notificationDTO.setAuthorId(authorId);

        socketSendOneUser(notificationDTO, accountId);
    }

    public void sendEmail(NotificationDTO notificationDTO) {
        log.info("NotificationService: sendEmail(NotificationDTO notificationDTO) startMethod, notificationDTO: {}",
                notificationDTO);
        UUID accountId = notificationDTO.getAuthorId();
        notificationDTO.setContent(SEND_EMAIL_MESSAGE);

        socketSendOneUser(notificationDTO, accountId);
    }

    public void sendPostComment(NotificationDTO notificationDTO) {
        log.info("NotificationService: sendPostComment(NotificationDTO notificationDTO) startMethod");

        Comment comment = commentService.getByAuthorIdAndTime(notificationDTO.getAuthorId(), notificationDTO.getSentTime());

        PostDto postDto = postService.get(comment.getPostId());
        UUID accountId = postDto.getAuthorId();

        log.info("NotificationService: sendPostComment(NotificationDTO notificationDTO) получен UUID автора поста: {}",
                accountId);

        if (!accountId.equals(notificationDTO.getAuthorId())) {
            socketSendOneUser(notificationDTO, accountId);
        }
    }

    public void addNotification(EventNotificationDTO eventNotificationDTO) {
        log.info("NotificationService: addNotification() startMethod, EventNotificationDTO: {}", eventNotificationDTO);
        eventNotificationRepository.save(notificationMapper.dtoToModel(eventNotificationDTO));
    }

    public void sendCommentComment(NotificationDTO notificationDTO) {
        log.info("NotificationService: sendCommentComment(NotificationDTO notificationDTO) startMethod");

        Comment comment = commentService.getByAuthorIdAndTime(notificationDTO.getAuthorId(), notificationDTO.getSentTime());
        CommentDto commentParent = commentService.get(comment.getParentId());
        UUID accountId = commentParent.getAuthorId();
        log.info("NotificationService: sendCommentComment(NotificationDTO notificationDTO) получен UUID автора поста: {}",
                accountId);

        if (!accountId.equals(notificationDTO.getAuthorId())) {
            socketSendOneUser(notificationDTO, accountId);
        }
    }

    public NotificationsDTO getAll() {
        UUID userId = AuthUtil.getUserId();
        log.info("NotificationService: getAll() startMethod, получен UUID: {}", userId);
        NotificationsDTO notificationsDTO = notificationsMapper.getEmptyAllNotificationsDTO(userId);

        Specification spec = SpecificationUtils.equal(EventNotification_.RECEIVER_ID, userId)
                .and(SpecificationUtils.equal(EventNotification_.STATUS, Status.SEND));

        List<EventNotification> userNotificationsSpec = eventNotificationRepository.findAll(spec);
        log.info("NotificationService: getAll() получен список нотификаций: {} для UUID: {}",
                userNotificationsSpec, userId);

        for(EventNotification eventNotification : userNotificationsSpec) {
            notificationsDTO.getContent().add(notificationsMapper.eventNotificationToContentDTO(eventNotification));
        }

        return notificationsDTO;
    }

    public void setAllReaded() {
        UUID userId = AuthUtil.getUserId();

        Specification spec = SpecificationUtils.equal(EventNotification_.RECEIVER_ID, userId)
                .and(SpecificationUtils.equal(EventNotification_.STATUS, Status.SEND));

        List<EventNotification> userNotifications = eventNotificationRepository.findAll(spec);
        log.info("NotificationService: setAllReaded() received unRead userNotifications: {} для UUID: {}",
                userNotifications, userId);

        for(EventNotification eNotification:userNotifications) {
            eNotification.setStatus(Status.READED);
            eventNotificationRepository.save(eNotification);
            log.info("NotificationService: setAllReaded() save update eventNotification: {}", eNotification);
        }
    }

    public CountDTO getCount() {
        UUID userId = AuthUtil.getUserId();
        log.info("NotificationService: getCount() startMethod, received UUID: {}", userId);
        return notificationsMapper.getCountDTO(eventNotificationRepository
                .countByReceiverIdAndStatusIs(userId, Status.SEND));
    }

    public boolean sendToWebsocket(NotificationDTO notificationDTO, UUID accountId) {
        try {
            List<WebSocketSession> sendingList = webSocketHandler.getSessionMap().getOrDefault(accountId, new ArrayList<>());
            if (sendingList.isEmpty()) {return false;}
            webSocketHandler.handleTextMessage(sendingList.get(0),
                    new TextMessage(notificationsMapper.getSocketNotificationJSON(notificationDTO, accountId)));
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}