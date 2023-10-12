package ru.skillbox.diplom.group40.social.network.impl.service.notification;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import ru.skillbox.diplom.group40.social.network.api.dto.notification.*;
import ru.skillbox.diplom.group40.social.network.domain.notification.EventNotification;
import ru.skillbox.diplom.group40.social.network.domain.notification.Settings;
import ru.skillbox.diplom.group40.social.network.impl.exception.NotFoundException;
import ru.skillbox.diplom.group40.social.network.impl.mapper.notification.NotificationMapper;
import ru.skillbox.diplom.group40.social.network.impl.mapper.notification.NotificationsMapper;
import ru.skillbox.diplom.group40.social.network.impl.repository.notification.EventNotificationRepository;
import ru.skillbox.diplom.group40.social.network.impl.repository.notification.SettingsRepository;
import ru.skillbox.diplom.group40.social.network.impl.service.friend.FriendService;
import ru.skillbox.diplom.group40.social.network.impl.service.kafka.KafkaService;
import ru.skillbox.diplom.group40.social.network.impl.utils.auth.AuthUtil;
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
        List<UUID> allFriends = friendService.getAllFriendsById(notificationDTO.getAuthorId());                         //  UUID testSavedUuid = UUID.fromString("d15d527e-d425-42d8-8016-b3c904d9c5b9");
        log.info("\nNotificationService: create(NotificationDTO notificationDTO): Add List<UUID> allFriends: {}",
                allFriends);

        for(UUID accountId : allFriends) {
            Optional notificationSettingsOptional = notificationSettingsRepository.findByAccountId(accountId);          //.orElseThrow(()->new AccountException("BADREUQEST"))

            if (notificationSettingsOptional.isPresent()) {
                Settings notificationSettings = (Settings) notificationSettingsOptional.get();

                if(isNotificationTypeEnabled(notificationSettings, notificationDTO.getNotificationType())){                                                                                                  /*notificationSettings.isEnableMessage()*/
                    eventNotificationRepository.save(notificationsMapper
                            .createEventNotification(notificationDTO, accountId));

                    /**Блок отправки в сокет*/
                    kafkaService.sendSocketNotificationDTO(notificationsMapper
                            .getSocketNotificationDTO(notificationDTO, accountId));       // @Рабочий - для кафки
//                    sendToWebsocket(notificationDTO, accountId);                        // Использовать при отключенной кафке
                    /**                     */
                }
            }

        }

    }

    public boolean sendToWebsocket(NotificationDTO notificationDTO, UUID accountId) {
        try {
            List<WebSocketSession> sendingList = webSocketHandler.getSessionMap().getOrDefault(accountId, new ArrayList<>());           //  webSocketHandler.getSessionMap().get(accountId)
            if (sendingList.isEmpty()) {return false;}
            webSocketHandler.handleTextMessage(sendingList.get(0),                                                      //  webSocketHandler.handleTextMessage(webSocketHandler.getSessionList().get(0),
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

        log.info("NotificationService: isNotificationTypeEnabled(): получен ответ: {}, для notificationType: {}",
                isNotificationTypeEnable, notificationType);
        return isNotificationTypeEnable;
    }

    public NotificationsDTO getAll() {
        UUID userId = AuthUtil.getUserId();
        log.info("NotificationService: getAll() startMethod, получен UUID: {}", userId);
        NotificationsDTO notificationsDTO = notificationsMapper.getEmptyAllNotificationsDTO(userId);

        ArrayList<EventNotification> userNotifications = (ArrayList<EventNotification>) eventNotificationRepository
                .findAllByReceiverIdAndStatusIs(userId, Status.SEND);
        log.debug("NotificationService: getAll() получен список нотификаций: {} для UUID: {}",
                userNotifications, userId);

        for(EventNotification eventNotification : userNotifications) {
            notificationsDTO.getContent().add(notificationsMapper.eventNotificationToContentDTO(eventNotification));
        }

        return notificationsDTO;
    }

    public void setAllReaded() {
        UUID userId = AuthUtil.getUserId();
        log.info("NotificationService: setAllReaded() startMethod, received UUID: {}", userId);
        ArrayList<EventNotification> userNotifications = (ArrayList<EventNotification>) eventNotificationRepository
                .findAllByReceiverIdAndStatusIs(userId, Status.SEND);

        log.debug("NotificationService: setAllReaded() received userNotifications: {}", userNotifications);
        for(EventNotification eNotification:userNotifications) {
            eNotification.setStatus(Status.READED);
            eventNotificationRepository.save(eNotification);
            log.debug("NotificationService: setAllReaded() save update eventNotification: {}", eNotification);
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
        return notificationSettingsRepository.findByAccountId(userId).orElseThrow(()
                -> new NotFoundException(NOT_FOUND_MESSAGE));
    }

//    @Transactional(readOnly = true)
    public void setSetting(SettingUpdateDTO settingUpdateDTO) {
        UUID userId = getUserId();
        log.info("NotificationService: setSetting(SettingUpdateDTO settingUpdateDTO) startMethod, received UUID: {}, " +
                        "settingUpdateDTO: {}", userId, settingUpdateDTO);

        Settings notificationSettings = notificationSettingsRepository.findByAccountId(userId).orElseThrow(()
                -> new NotFoundException(NOT_FOUND_MESSAGE));

        rewriteSettings(notificationSettings, settingUpdateDTO);

        notificationSettingsRepository.save(notificationSettings);

        log.info("NotificationService: setSetting(SettingUpdateDTO settingUpdateDTO) updated settings: {}, " +
                        "NotificationSettings: {}", userId, notificationSettings);

        /*
        Specification spec = SpecificationUtils.equal(Settings_.ENABLE_LIKE, settingUpdateDTO.getNotificationType())
                .or(SpecificationUtils.equal(Settings_.ENABLE_POST, settingUpdateDTO.getNotificationType()))
                .or(SpecificationUtils.equal(Settings_.ENABLE_POST_COMMENT, settingUpdateDTO.getNotificationType()))
                .or(SpecificationUtils.equal(Settings_.ENABLE_COMMENT_COMMENT, settingUpdateDTO.getNotificationType()))
                .or(SpecificationUtils.equal(Settings_.ENABLE_MESSAGE, settingUpdateDTO.getNotificationType()))
                .or(SpecificationUtils.equal(Settings_.ENABLE_FRIEND_REQUEST, settingUpdateDTO.getNotificationType()))
                .or(SpecificationUtils.equal(Settings_.ENABLE_FRIEND_BIRTHDAY, settingUpdateDTO.getNotificationType()))
                .or(SpecificationUtils.equal(Settings_.ENABLE_SEND_EMAIL_MESSAGE settingUpdateDTO.getNotificationType()));
        */
    }

    private void rewriteSettings(Settings notificationSettings, SettingUpdateDTO settingUpdateDTO) { // TODO: ??? Вынести в маппер?

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

        /*
        (settingUpdateDTO.getNotificationType()).equals(Settings_.ENABLE_LIKE) ? notificationSettings.setEnableLike(settingUpdateDTO.isEnable()) :
            (settingUpdateDTO.getNotificationType()).equals(Settings_.ENABLE_POST) ? notificationSettings.setEnablePost(settingUpdateDTO.isEnable()) :
                (settingUpdateDTO.getNotificationType()).equals(Settings_.ENABLE_POST_COMMENT) ? notificationSettings.setEnablePostComment(settingUpdateDTO.isEnable()) :
                    (settingUpdateDTO.getNotificationType()).equals(Settings_.ENABLE_COMMENT_COMMENT) ? notificationSettings.setEnableCommentComment(settingUpdateDTO.isEnable()) :
                        (settingUpdateDTO.getNotificationType()).equals(Settings_.ENABLE_MESSAGE) ? notificationSettings.setEnableMessage(settingUpdateDTO.isEnable()) :
                            (settingUpdateDTO.getNotificationType()).equals(Settings_.ENABLE_FRIEND_REQUEST) ? notificationSettings.setEnableFriendRequest(settingUpdateDTO.isEnable()) :
                                (settingUpdateDTO.getNotificationType()).equals(Settings_.ENABLE_FRIEND_BIRTHDAY) ? notificationSettings.setEnableFriendBirthday(settingUpdateDTO.isEnable()) :
                                    (settingUpdateDTO.getNotificationType()).equals(Settings_.ENABLE_SEND_EMAIL_MESSAGE) ? notificationSettings.setEnableSendEmailMessage(settingUpdateDTO.isEnable()) : return;
        */

    }

    private UUID getUserId() {
        UUID userId = AuthUtil.getUserId();
        log.info("NotificationService: getUserId() startMethod, UUID: {}", userId);
        return userId;
    }

    public Boolean createSettings(UUID id) {
            Settings notificationSettings = new Settings();
            notificationSettings.setAccountId(id);  // new Settings().setAccountId(id)
            settingsRepository.save(notificationSettings);
        return true;
    }

    public void addNotification(EventNotificationDTO eventNotificationDTO) {
        log.info("NotificationService: addNotification() startMethod, EventNotificationDTO: {}", eventNotificationDTO);
        eventNotificationRepository.save(notificationMapper.dtoToModel(eventNotificationDTO));
    }

}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////



//return mapperAccount.toDto(accountRepository.findById(userId).orElseThrow(()->new AccountException(BADREUQEST)));



//            System.out.println(eventNotification.toString());



        /*
        //AAA Вынести в сервис и далее, это ввод тестового значения:
        NotificationsDTO notificationsDTO = new NotificationsDTO();
        notificationsDTO.setTotalPages(1);
        notificationsDTO.setTotalElements(2);
        notificationsDTO.setNumber(3);
        notificationsDTO.setSize(4);

        //  ДЛЯ notificationsDTO.setContent()
        ArrayList<ContentDTO> testContents = new ArrayList<>();
        ContentDTO testContent = new ContentDTO();
        testContent.setTimeStamp(LocalDateTime.now());
        NotificationDTO notification = new NotificationDTO();
        notification.setId(UUID.fromString("c11111cb-7b09-4abf-a520-b4cd4129458c"));
        notification.setIsDeleted(false);
        notification.setAuthorId(UUID.fromString("c05210cb-7b09-4abf-a520-b4cd4129458c"));
        notification.setContent("TestContent");
        notification.setType(Type.POST);
        notification.setSentTime(LocalDateTime.now());
        testContent.setData(notification);
        testContents.add(testContent);
        //  /ДЛЯ
        notificationsDTO.setContent(testContents);

        //  ДЛЯ notificationsDTO.setSort(testSort)
        SortDTO testSort = new SortDTO();
        testSort.setEmpty(true);
        testSort.setSorted(true);
        testSort.setUnsorted(true);
        //  /ДЛЯ
        notificationsDTO.setSort(testSort);

        notificationsDTO.setFirst(true);
        notificationsDTO.setLast(true);
        notificationsDTO.setNumberOfElements(5);

        //notificationsDTO.setPageable();

        notificationsDTO.setEmpty(true);

        //AAA


        return notificationsDTO;
        */



//    List<EventNotification> messageList = new ArrayList<>();
//    //1new
//    EventNotification eventNotification = notificationsMapper
//            .createEventNotification(notificationDTO, accountId);
//                eventNotificationRepository.save(eventNotification);
//                        //1new
//
//                        if(notificationSettings.isEnableMessage()){
//                        messageList.add(eventNotification);
//                        }



   /* public CountDTO getCount() {
        UUID userId = AuthUtil.getUserId();
        log.info("NotificationService: getCount() startMethod, received UUID: {}", userId);

        //1
        PartCountDTO partCountDTO = new PartCountDTO();
        partCountDTO.setCount(eventNotificationRepository.countByReceiverIdAndStatusIs(userId, Status.SEND));

        CountDTO countDTO = new CountDTO();
        countDTO.setTimeStamp(LocalDateTime.now());
        countDTO.setData(partCountDTO);

        return countDTO;
        //1
    }*/