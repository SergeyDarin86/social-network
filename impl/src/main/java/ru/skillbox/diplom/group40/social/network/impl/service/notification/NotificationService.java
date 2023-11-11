package ru.skillbox.diplom.group40.social.network.impl.service.notification;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skillbox.diplom.group40.social.network.api.dto.notification.*;
import ru.skillbox.diplom.group40.social.network.domain.notification.EventNotification;
import ru.skillbox.diplom.group40.social.network.domain.notification.EventNotification_;
import ru.skillbox.diplom.group40.social.network.domain.notification.Settings;
import ru.skillbox.diplom.group40.social.network.impl.mapper.notification.NotificationMapper;
import ru.skillbox.diplom.group40.social.network.impl.mapper.notification.NotificationsMapper;
import ru.skillbox.diplom.group40.social.network.impl.repository.notification.EventNotificationRepository;
import ru.skillbox.diplom.group40.social.network.impl.repository.notification.SettingsRepository;
import ru.skillbox.diplom.group40.social.network.impl.service.friend.FriendService;
import ru.skillbox.diplom.group40.social.network.impl.service.kafka.KafkaService;
import ru.skillbox.diplom.group40.social.network.impl.utils.auth.AuthUtil;
import ru.skillbox.diplom.group40.social.network.impl.utils.specification.SpecificationUtils;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
//@RequiredArgsConstructor
public class NotificationService {
    private final List<NotificationHandler> notificationHandlerList;
    private Map<Type, NotificationHandler> notificationHandlersMap;
    private final SettingsRepository notificationSettingsRepository;
    private final NotificationSettingsService notificationSettingsService;
    private final EventNotificationRepository eventNotificationRepository;
    private final SettingsRepository settingsRepository;
    private final KafkaService kafkaService;
    private final NotificationsMapper notificationsMapper;
    private final NotificationMapper notificationMapper;

    /** Вынести в компонены */
    private static final String SEND_LIKE_POST = "поставил LIKE на Вашу запись \"";
    private static final String SEND_LIKE_COMMENT = "поставил LIKE на Ваш комментарий \"";
    private static final String SEND_EMAIL_MESSAGE = "Вам на почту отправлена ссылка для восстановления пароля";
    private static final String SEND_FRIEND_REQUEST_MESSAGE = "получен запрос на добавление в друзья от ";
    private static final String SEND_FRIEND_REQUEST_MESSAGE2 = "хочет добавить Вас в друзья";
    private static final String SEND_FRIEND_REQUEST_MESSAGE3 = " добавил Вас в друзья";
    private static final String SEND_FRIEND_REQUEST_MESSAGE4 = " заблокировал Вас";
    private static final String SEND_FRIEND_REQUEST_MESSAGE5 = " разблокировал Вас";
    private static final String SEND_FRIEND_REQUEST_MESSAGE6 = " подписался на Вас";
    /**  */

    /** Конструктор для внедрения МАПы */
//    /*
    public NotificationService(
            List<NotificationHandler> notificationHandlerList,
            SettingsRepository notificationSettingsRepository,
            NotificationSettingsService notificationSettingsService,
            EventNotificationRepository eventNotificationRepository,
            SettingsRepository settingsRepository,
            KafkaService kafkaService,
            NotificationsMapper notificationsMapper,
            NotificationMapper notificationMapper
    ) {

        this.notificationHandlerList = notificationHandlerList;
        this.notificationHandlersMap = this.notificationHandlerList.stream().collect(Collectors.toMap(NotificationHandler::myType,
                        notificationHandler -> notificationHandler));
        this.notificationSettingsRepository = notificationSettingsRepository;
        this.notificationSettingsService = notificationSettingsService;
        this.eventNotificationRepository = eventNotificationRepository;
        this.settingsRepository = settingsRepository;
        this.kafkaService = kafkaService;
        this.notificationsMapper = notificationsMapper;
        this.notificationMapper = notificationMapper;

    }
//    */
    /** */

    public void create(NotificationDTO notificationDTO) {
        log.info("NotificationService: create(NotificationDTO notificationDTO) startMethod, notificationDTO: {}",
                notificationDTO);

        socketSend(notificationHandlersMap.get(notificationDTO.getNotificationType())
                .getEventNotificationList(notificationDTO));

    }

    public void socketSend(List<EventNotification> listEventNotifications) {
        log.info("NotificationService: socketSend(List<EventNotification> listEventNotifications) startMethod, " +
                        "получен List<EventNotification>: {}", listEventNotifications);
        eventNotificationRepository.saveAll(listEventNotifications);
        kafkaService.sendListSocketNotificationDTO(notificationsMapper.getListSocketNotificationDTO(listEventNotifications));
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

        /*
        UUID likeId = notificationDTO.getAuthorId();
        Like like = likeService.getLike(likeId);
        UUID accountId = null;

        if (like.getType().equals(LikeType.POST)) {
            Post post = (Post) postRepository.findById(like.getItemId()).orElseThrow(()
                    -> new NotFoundException("notFoundPostMessage"));
            accountId = post.getAuthorId();
            notificationDTO.setContent(SEND_LIKE_POST.concat(post.getPostText().concat("\"")));
        }

        if (like.getType().equals(LikeType.COMMENT)) {
            CommentDto commentDto = commentService.get(like.getItemId());
            accountId = commentDto.getAuthorId();
            log.info("NotificationService: sendLike(NotificationDTO notificationDTO) получен UUID автора COMMENT'а: {}",
                    accountId);
            notificationDTO.setContent(SEND_LIKE_COMMENT.concat(commentDto.getCommentText()).concat("\""));
        }

        UUID authorId = like.getAuthorId();
        socketSendOneUser(notificationDTO, accountId);
            */

//        UUID accountId =  like.getLike(notificationDTO);
//        socketSendOneUser(notificationDTO, accountId);
    }

    /*
    public void sendAllFriend(NotificationDTO notificationDTO) {
        log.info("NotificationService: sendAllFriend(NotificationDTO notificationDTO) startMethod");
        List<UUID> allFriends = notificationsMapper.getListUUID(friendService.getAllFriendsById(notificationDTO.getAuthorId()));
        log.info("NotificationService: sendAllFriend(_): Add List<UUID> allFriends: {}",
                allFriends);
        for(UUID accountId : allFriends) {
            socketSendOneUser(notificationDTO, accountId);
        }
    }
    */

    public void sendMeFriendRequest(NotificationDTO notificationDTO) {
        log.info("NotificationService: sendMeFriendRequest(NotificationDTO notificationDTO) startMethod, notificationDTO: {}",
                notificationDTO);

        UUID accountIdFrom = notificationDTO.getAuthorId();     // accountIdFrom
        UUID accountId = UUID.fromString(notificationDTO.getContent());   // accountIdTo
        notificationDTO.setContent(SEND_FRIEND_REQUEST_MESSAGE2);

        socketSendOneUser(notificationDTO, accountId);
    }

    public void sendMeFriendRequestApprove(NotificationDTO notificationDTO) {
        log.info("NotificationService: sendMeFriendRequest(NotificationDTO notificationDTO) startMethod, notificationDTO: {}",
                notificationDTO);

        UUID accountIdFrom = notificationDTO.getAuthorId();     // accountIdFrom
        UUID accountId = UUID.fromString(notificationDTO.getContent());   // accountIdTo
        notificationDTO.setContent(SEND_FRIEND_REQUEST_MESSAGE3);

        socketSendOneUser(notificationDTO, accountId);
    }

    public void sendMeFriendBlocked(NotificationDTO notificationDTO) {
        log.info("NotificationService: sendMeFriendRequest(NotificationDTO notificationDTO) startMethod, notificationDTO: {}",
                notificationDTO);

        UUID accountIdFrom = notificationDTO.getAuthorId();     // accountIdFrom
        UUID accountId = UUID.fromString(notificationDTO.getContent());   // accountIdTo
        notificationDTO.setContent(SEND_FRIEND_REQUEST_MESSAGE4);

        socketSendOneUser(notificationDTO, accountId);
    }

    public void sendMeFriendUnBlocked(NotificationDTO notificationDTO) {
        log.info("NotificationService: sendMeFriendRequest(NotificationDTO notificationDTO) startMethod, notificationDTO: {}",
                notificationDTO);

        UUID accountIdFrom = notificationDTO.getAuthorId();     // accountIdFrom
        UUID accountId = UUID.fromString(notificationDTO.getContent());   // accountIdTo
        notificationDTO.setContent(SEND_FRIEND_REQUEST_MESSAGE5);

        socketSendOneUser(notificationDTO, accountId);
    }

    public void sendMeFriendSubscribe(NotificationDTO notificationDTO) {
        log.info("NotificationService: sendMeFriendRequest(NotificationDTO notificationDTO) startMethod, notificationDTO: {}",
                notificationDTO);

        UUID accountIdFrom = notificationDTO.getAuthorId();     // accountIdFrom
        UUID accountId = UUID.fromString(notificationDTO.getContent());   // accountIdTo
        notificationDTO.setContent(SEND_FRIEND_REQUEST_MESSAGE6);

        socketSendOneUser(notificationDTO, accountId);
    }


    public void sendMessage(NotificationDTO notificationDTO) {
        log.info("NotificationService: sendMessage(NotificationDTO notificationDTO) startMethod, notificationDTO: {}",
                notificationDTO);

        /*
        UUID messageId = notificationDTO.getAuthorId();
        Message message = messageService.getMessage(messageId);

        UUID authorId = message.getConversationPartner1();
        UUID accountId = message.getConversationPartner2();
        notificationDTO.setContent(notificationDTO.getContent());
        notificationDTO.setAuthorId(authorId);
        socketSendOneUser(notificationDTO, accountId);
        */

//        UUID accountId = message.getMessage(notificationDTO);
//        socketSendOneUser(notificationDTO, accountId);
    }

    public void sendEmail(NotificationDTO notificationDTO) {
        log.info("NotificationService: sendEmail(NotificationDTO notificationDTO) startMethod, notificationDTO: {}",
                notificationDTO);
        UUID accountId = notificationDTO.getAuthorId();
        notificationDTO.setContent(SEND_EMAIL_MESSAGE);

        socketSendOneUser(notificationDTO, accountId);
    }

    /*
    public void sendPostComment(NotificationDTO notificationDTO) {
        log.info("NotificationService: sendPostComment(NotificationDTO notificationDTO) startMethod");

        //


        Comment comment = commentService.getByAuthorIdAndTime(notificationDTO.getAuthorId(), notificationDTO.getSentTime().toLocalDateTime());


//        PostDto postDto = postService.get(comment.getPostId());
//        UUID accountId = postDto.getAuthorId();


        Post post = (Post) postRepository.findById(comment.getPostId()).orElseThrow(()
                -> new NotFoundException("notFoundPostMessage"));
        UUID accountId = post.getAuthorId();

        log.info("NotificationService: sendPostComment(NotificationDTO notificationDTO) получен UUID автора поста: {}",
                accountId);
        socketSendOneUser(notificationDTO, accountId);


        //

//        PostComment postComment = new PostComment();

        UUID accountId = postComment.getPostComment(notificationDTO);
        socketSendOneUser(notificationDTO, accountId);
    }
    */

    public void sendCommentComment(NotificationDTO notificationDTO) {
        log.info("NotificationService: sendCommentComment(NotificationDTO notificationDTO) startMethod");

        /*
        Comment comment = commentService.getByAuthorIdAndTime(notificationDTO.getAuthorId(), notificationDTO.getSentTime().toLocalDateTime());
        CommentDto commentParent = commentService.get(comment.getParentId());
        UUID accountId = commentParent.getAuthorId();
        log.info("NotificationService: sendCommentComment(NotificationDTO notificationDTO) получен UUID автора поста: {}",
                accountId);

        socketSendOneUser(notificationDTO, accountId);
        */
//        UUID accountId = commentComment.getCommentComment(notificationDTO);
//        socketSendOneUser(notificationDTO, accountId);
    }



    private boolean isNotificationTypeEnables(Settings notificationSettings, Type notificationType) {

        boolean isNotificationTypeEnable = false;

        switch (notificationType) {
            case LIKE:
                isNotificationTypeEnable = notificationSettings.isEnableLike();
                break;
            case POST:
                isNotificationTypeEnable = notificationSettings.isEnablePost();
                break;
            case POST_COMMENT:
                isNotificationTypeEnable = notificationSettings.isEnablePostComment();
                break;
            case COMMENT_COMMENT:
                isNotificationTypeEnable = notificationSettings.isEnableCommentComment();
                break;
            case MESSAGE:
                isNotificationTypeEnable = notificationSettings.isEnableMessage();
                break;
            case FRIEND_REQUEST:
                isNotificationTypeEnable = notificationSettings.isEnableFriendRequest();
                break;
            case FRIEND_BIRTHDAY:
                isNotificationTypeEnable = notificationSettings.isEnableFriendBirthday();
                break;
            case SEND_EMAIL_MESSAGE:
                isNotificationTypeEnable = notificationSettings.isEnableSendEmailMessage();
                break;


            default:
                isNotificationTypeEnable = false;
        }

        log.info("NotificationService: isNotificationTypeEnables(): получен ответ: {}, для notificationType: {}",
                isNotificationTypeEnable, notificationType);
        return isNotificationTypeEnable;
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

    public Settings getSettings() {
        UUID userId = AuthUtil.getUserId();
        log.info("NotificationService: getSettings() startMethod, received UUID: {}", userId);
        return notificationSettingsRepository.findByAccountId(userId);
    }

    public void setSetting(SettingUpdateDTO settingUpdateDTO) {
        UUID userId = getUserId();
        log.info("NotificationService: setSetting(SettingUpdateDTO settingUpdateDTO) startMethod, received UUID: {}, " +
                        "settingUpdateDTO: {}", userId, settingUpdateDTO);

        Settings notificationSettings = notificationSettingsRepository.findByAccountId(userId);

        rewriteSettings(notificationSettings, settingUpdateDTO);

        notificationSettingsRepository.save(notificationSettings);

        log.info("NotificationService: setSetting(SettingUpdateDTO settingUpdateDTO) updated settings: {}, " +
                        "NotificationSettings: {}", userId, notificationSettings);

    }

    private void rewriteSettings(Settings notificationSettings, SettingUpdateDTO settingUpdateDTO) {

    if (settingUpdateDTO.getNotificationType().equals(Type.LIKE)) {
        notificationSettings.setEnableLike(settingUpdateDTO.isEnable());
    }

    if (settingUpdateDTO.getNotificationType().equals(Type.POST)) {
        notificationSettings.setEnablePost(settingUpdateDTO.isEnable());
    }

    if (settingUpdateDTO.getNotificationType().equals(Type.POST_COMMENT)) {
        notificationSettings.setEnablePostComment(settingUpdateDTO.isEnable());
    }

    if (settingUpdateDTO.getNotificationType().equals(Type.COMMENT_COMMENT)) {
        notificationSettings.setEnableCommentComment(settingUpdateDTO.isEnable());
    }

    if (settingUpdateDTO.getNotificationType().equals(Type.MESSAGE)) {
        notificationSettings.setEnableMessage(settingUpdateDTO.isEnable());
    }

    if (settingUpdateDTO.getNotificationType().equals(Type.FRIEND_REQUEST)) {
        notificationSettings.setEnableFriendRequest(settingUpdateDTO.isEnable());
    }

    if (settingUpdateDTO.getNotificationType().equals(Type.FRIEND_BIRTHDAY)) {
        notificationSettings.setEnableFriendBirthday(settingUpdateDTO.isEnable());
    }

    if (settingUpdateDTO.getNotificationType().equals(Type.SEND_EMAIL_MESSAGE)) {
        notificationSettings.setEnableSendEmailMessage(settingUpdateDTO.isEnable());
    }
    }

    private UUID getUserId() {
        UUID userId = AuthUtil.getUserId();
        log.info("NotificationService: getUserId() startMethod, UUID: {}", userId);
        return userId;
    }

    public Boolean createSettings(UUID id) {
            Settings notificationSettings = new Settings();
            notificationSettings.setAccountId(id);
            settingsRepository.save(notificationSettings);
        log.info("NotificationService: createSettings() create NotificationSettings: {}", notificationSettings);
        return true;
    }

    public void addNotification(EventNotificationDTO eventNotificationDTO) {
        log.info("NotificationService: addNotification() startMethod, EventNotificationDTO: {}", eventNotificationDTO);
        eventNotificationRepository.save(notificationMapper.dtoToModel(eventNotificationDTO));
    }

    private boolean isNotificationTypeEnabled(Settings notificationSettings, Type notificationType) {

        boolean isNotificationTypeEnable = false;

        if (notificationType.equals(Type.LIKE)) {
            isNotificationTypeEnable = notificationSettings.isEnableLike();
        }

        if (notificationType.equals(Type.POST)) {
            isNotificationTypeEnable = notificationSettings.isEnablePost();
        }

        if (notificationType.equals(Type.POST_COMMENT)) {
            isNotificationTypeEnable = notificationSettings.isEnablePostComment();
        }

        if (notificationType.equals(Type.COMMENT_COMMENT)) {
            isNotificationTypeEnable = notificationSettings.isEnableCommentComment();
        }

        if (notificationType.equals(Type.MESSAGE)) {
            isNotificationTypeEnable = notificationSettings.isEnableMessage();
        }

        if (notificationType.equals(Type.FRIEND_REQUEST)) {
            isNotificationTypeEnable = notificationSettings.isEnableFriendRequest();
        }

        if (notificationType.equals(Type.FRIEND_BIRTHDAY)) {
            isNotificationTypeEnable = notificationSettings.isEnableFriendBirthday();
        }

        if (notificationType.equals(Type.SEND_EMAIL_MESSAGE)) {
            isNotificationTypeEnable = notificationSettings.isEnableSendEmailMessage();
        }

        log.info("NotificationService: isNotificationTypeEnabled(): получен ответ: {}, для notificationType: {}",
                isNotificationTypeEnable, notificationType);
        return isNotificationTypeEnable;
    }
}