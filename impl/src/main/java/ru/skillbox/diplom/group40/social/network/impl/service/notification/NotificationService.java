package ru.skillbox.diplom.group40.social.network.impl.service.notification;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import ru.skillbox.diplom.group40.social.network.api.dto.notification.*;
import ru.skillbox.diplom.group40.social.network.domain.notification.EventNotification;
import ru.skillbox.diplom.group40.social.network.domain.notification.EventNotification_;
import ru.skillbox.diplom.group40.social.network.domain.notification.Settings;
import ru.skillbox.diplom.group40.social.network.impl.exception.NotFoundException;
import ru.skillbox.diplom.group40.social.network.impl.mapper.notification.NotificationMapper;
import ru.skillbox.diplom.group40.social.network.impl.mapper.notification.NotificationsMapper;
import ru.skillbox.diplom.group40.social.network.impl.repository.notification.EventNotificationRepository;
import ru.skillbox.diplom.group40.social.network.impl.repository.notification.SettingsRepository;
import ru.skillbox.diplom.group40.social.network.impl.service.friend.FriendService;
import ru.skillbox.diplom.group40.social.network.impl.service.kafka.KafkaService;
import ru.skillbox.diplom.group40.social.network.impl.utils.auth.AuthUtil;
import ru.skillbox.diplom.group40.social.network.impl.utils.specification.SpecificationUtils;
import ru.skillbox.diplom.group40.social.network.impl.utils.websocket.WebSocketHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class NotificationService {                                                                                      // TODO бросить индекс - уточнить на что - receiver_id???

    private final SettingsRepository notificationSettingsRepository;
    private final EventNotificationRepository eventNotificationRepository;
    private final SettingsRepository settingsRepository;
    private final FriendService friendService;
    private final NotificationsMapper notificationsMapper;
    private final NotificationMapper notificationMapper;
    private static final String NOT_FOUND_MESSAGE = "Настройки нотификаций пользователя не найдены";
    private final WebSocketHandler webSocketHandler;
    private final KafkaService kafkaService;

    public void create(NotificationDTO notificationDTO) {
        log.info("\nNotificationService: create(NotificationDTO notificationDTO) startMethod, notificationDTO: {}",
                notificationDTO);
//        List<UUID> allFriends = friendService.getAllFriendsById(notificationDTO.getAuthorId());
        List<UUID> allFriends = notificationsMapper.getListUUID(friendService.getAllFriendsById(notificationDTO.getAuthorId())); // TODO: Подставить перемапленный лист
        log.info("\nNotificationService: create(NotificationDTO notificationDTO): Add List<UUID> allFriends: {}",
                allFriends);


        for(UUID accountId : allFriends) {
            Optional notificationSettingsOptional = notificationSettingsRepository.findByAccountId(accountId);          //.orElseThrow(()->new AccountException("BADREUQEST"))

            if (notificationSettingsOptional.isPresent()) {
                Settings notificationSettings = (Settings) notificationSettingsOptional.get();

                if(isNotificationTypeEnabled(notificationSettings, notificationDTO.getNotificationType())){
                    eventNotificationRepository.save(notificationsMapper
                            .createEventNotification(notificationDTO, accountId));

                    kafkaService.sendSocketNotificationDTO(notificationsMapper
                            .getSocketNotificationDTO(notificationDTO, accountId));
                }
            }

        }

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

        log.info("\nNotificationService: isNotificationTypeEnabled(): получен ответ: {}, для notificationType: {}",
                isNotificationTypeEnable, notificationType);
        return isNotificationTypeEnable;
    }

    public NotificationsDTO getAll() {
        UUID userId = AuthUtil.getUserId();
        log.info("\nNotificationService: getAll() startMethod, получен UUID: {}", userId);
        NotificationsDTO notificationsDTO = notificationsMapper.getEmptyAllNotificationsDTO(userId);

        Specification spec = SpecificationUtils.equal(EventNotification_.RECEIVER_ID, userId)
                .and(SpecificationUtils.equal(EventNotification_.STATUS, Status.SEND));

        List<EventNotification> userNotificationsSpec = eventNotificationRepository.findAll(spec);
        log.info("\nNotificationService: getAll() получен список нотификаций: {} для UUID: {}",
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
        log.info("\nNotificationService: setAllReaded() received unRead userNotifications: {} для UUID: {}",
                userNotifications, userId);

        for(EventNotification eNotification:userNotifications) {
            eNotification.setStatus(Status.READED);
            eventNotificationRepository.save(eNotification);
            log.info("\nNotificationService: setAllReaded() save update eventNotification: {}", eNotification);
        }
    }

    public CountDTO getCount() {
        UUID userId = AuthUtil.getUserId();
        log.info("\nNotificationService: getCount() startMethod, received UUID: {}", userId);
        return notificationsMapper.getCountDTO(eventNotificationRepository
                .countByReceiverIdAndStatusIs(userId, Status.SEND));
    }

    public Settings getSettings() {
        UUID userId = AuthUtil.getUserId();
        log.info("\nNotificationService: getSettings() startMethod, received UUID: {}", userId);
        return notificationSettingsRepository.findByAccountId(userId).orElseThrow(()
                -> new NotFoundException(NOT_FOUND_MESSAGE));
    }

    public void setSetting(SettingUpdateDTO settingUpdateDTO) {
        UUID userId = getUserId();
        log.info("\nNotificationService: setSetting(SettingUpdateDTO settingUpdateDTO) startMethod, received UUID: {}, " +
                        "settingUpdateDTO: {}", userId, settingUpdateDTO);

        Settings notificationSettings = notificationSettingsRepository.findByAccountId(userId).orElseThrow(()
                -> new NotFoundException(NOT_FOUND_MESSAGE));

        rewriteSettings(notificationSettings, settingUpdateDTO);

        notificationSettingsRepository.save(notificationSettings);

        log.info("\nNotificationService: setSetting(SettingUpdateDTO settingUpdateDTO) updated settings: {}, " +
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
        log.info("\nNotificationService: getUserId() startMethod, UUID: {}", userId);
        return userId;
    }

    public Boolean createSettings(UUID id) {
            Settings notificationSettings = new Settings();
            notificationSettings.setAccountId(id);
            settingsRepository.save(notificationSettings);
        log.info("\nNotificationService: createSettings() create NotificationSettings: {}", notificationSettings);
        return true;
    }

    public void addNotification(EventNotificationDTO eventNotificationDTO) {
        log.info("\nNotificationService: addNotification() startMethod, EventNotificationDTO: {}", eventNotificationDTO);
        eventNotificationRepository.save(notificationMapper.dtoToModel(eventNotificationDTO));
    }
}