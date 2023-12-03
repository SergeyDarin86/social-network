package ru.skillbox.diplom.group40.social.network.impl.service.notification;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skillbox.diplom.group40.social.network.api.dto.notification.*;
import ru.skillbox.diplom.group40.social.network.domain.notification.EventNotification;
import ru.skillbox.diplom.group40.social.network.domain.notification.EventNotification_;
import ru.skillbox.diplom.group40.social.network.impl.mapper.notification.NotificationMapper;
import ru.skillbox.diplom.group40.social.network.impl.mapper.notification.NotificationsMapper;
import ru.skillbox.diplom.group40.social.network.impl.repository.notification.EventNotificationRepository;
import ru.skillbox.diplom.group40.social.network.impl.service.kafka.KafkaService;
import ru.skillbox.diplom.group40.social.network.impl.utils.auth.AuthUtil;
import ru.skillbox.diplom.group40.social.network.impl.utils.specification.SpecificationUtils;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class NotificationService {
    private final List<NotificationHandler> notificationHandlerList;
    private final Map<Type, NotificationHandler> notificationHandlersMap;
    private final EventNotificationRepository eventNotificationRepository;
    private final KafkaService kafkaService;
    private final NotificationsMapper notificationsMapper;
    private final NotificationMapper notificationMapper;

    public NotificationService(
            List<NotificationHandler> notificationHandlerList,
            EventNotificationRepository eventNotificationRepository,
            KafkaService kafkaService,
            NotificationsMapper notificationsMapper,
            NotificationMapper notificationMapper
    ) {
        this.notificationHandlerList = notificationHandlerList;
        this.notificationHandlersMap = this.notificationHandlerList.stream().collect(Collectors.toMap(NotificationHandler::myType,
                        notificationHandler -> notificationHandler));
        this.eventNotificationRepository = eventNotificationRepository;
        this.kafkaService = kafkaService;
        this.notificationsMapper = notificationsMapper;
        this.notificationMapper = notificationMapper;
    }

    public void create(NotificationDTO notificationDTO) {
        log.info("NotificationService: create(NotificationDTO notificationDTO) startMethod, notificationDTO: {}",
                notificationDTO);
        send(eventNotificationRepository.saveAll(notificationHandlersMap.get(notificationDTO.getNotificationType())
                .getEventNotificationList(notificationDTO)));
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

    public Page<ContentDTO> getAllNew(Pageable page) {
        UUID userId = AuthUtil.getUserId();
        log.info("NotificationService: getAllNew() startMethod, получен UUID: {}", userId);

        Specification spec = SpecificationUtils.equal(EventNotification_.RECEIVER_ID, userId)
                .and(SpecificationUtils.equal(EventNotification_.STATUS, Status.SEND));

        Page<EventNotification> userNotificationsSpecPage = eventNotificationRepository.findAll(spec, page);
        Page<ContentDTO> userNotificationsContentDTOsPage = userNotificationsSpecPage
                .map(notificationsMapper::eventNotificationToContentDTO);

        log.info("NotificationService: getAllNew() к возврату, для UUID: {}, получен Page<ContentDTO>: {}",
                userId, userNotificationsContentDTOsPage);
        return userNotificationsContentDTOsPage;
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

    public void addNotification(EventNotificationDTO eventNotificationDTO) {
        log.info("NotificationService: addNotification() startMethod, EventNotificationDTO: {}", eventNotificationDTO);
        eventNotificationRepository.save(notificationMapper.dtoToModel(eventNotificationDTO));
    }

    public Timestamp getLastTimestamp() {
        log.info("NotificationService: getLastTimestamp() startMethod");
        Timestamp lastTimestamp = eventNotificationRepository.findTopDate()
                .orElse(new Timestamp(System.currentTimeMillis()));
        log.info("NotificationService: getLastTimestamp() получен LastTime Timestamp: {}", lastTimestamp);
        return lastTimestamp;
    }

    private void send(List<EventNotification> listEventNotifications) {
        log.info("NotificationService: send(List<EventNotification> listEventNotifications) startMethod, " +
                "получен List<EventNotification>: {}", listEventNotifications);
        kafkaService.sendListSocketNotificationDTO(notificationsMapper
                .getListSocketNotificationDTO(listEventNotifications));
    }
}