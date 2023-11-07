package ru.skillbox.diplom.group40.social.network.impl.service.notification.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skillbox.diplom.group40.social.network.api.dto.notification.NotificationDTO;
import ru.skillbox.diplom.group40.social.network.domain.notification.Settings;
import ru.skillbox.diplom.group40.social.network.impl.mapper.notification.NotificationsMapper;
import ru.skillbox.diplom.group40.social.network.impl.repository.notification.EventNotificationRepository;
import ru.skillbox.diplom.group40.social.network.impl.repository.notification.SettingsRepository;
import ru.skillbox.diplom.group40.social.network.impl.service.friend.FriendService;
import ru.skillbox.diplom.group40.social.network.impl.service.kafka.KafkaService;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class Post {
    private final SettingsRepository notificationSettingsRepository;
    private final EventNotificationRepository eventNotificationRepository;
    private final NotificationsMapper notificationsMapper;
    private final FriendService friendService;
    private final KafkaService kafkaService;

    public void sendAllFriend(NotificationDTO notificationDTO) {
        log.info("NotificationService: sendAllFriend(NotificationDTO notificationDTO) startMethod");
        List<UUID> allFriends = notificationsMapper.getListUUID(friendService.getAllFriendsById(notificationDTO.getAuthorId()));
        log.info("NotificationService: sendAllFriend(_): Add List<UUID> allFriends: {}",
                allFriends);
        for(UUID accountId : allFriends) {
            socketSendOneUser(notificationDTO, accountId);
        }
    }

    public void socketSendOneUser(NotificationDTO notificationDTO, UUID accountId) {
        Settings notificationSettings = notificationSettingsRepository.findByAccountId(accountId);
//        if(isNotificationTypeEnables(notificationSettings, notificationDTO.getNotificationType())){
        if(notificationsMapper.isNotificationTypeEnables(notificationSettings, notificationDTO.getNotificationType())){
            eventNotificationRepository.save(notificationsMapper
                    .createEventNotification(notificationDTO, accountId));

            kafkaService.sendSocketNotificationDTO(notificationsMapper
                    .getSocketNotificationDTO(notificationDTO, accountId));
        }
    }


}
