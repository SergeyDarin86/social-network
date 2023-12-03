package ru.skillbox.diplom.group40.social.network.impl.mapper.friend;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.skillbox.diplom.group40.social.network.api.dto.friend.FriendDto;
import ru.skillbox.diplom.group40.social.network.api.dto.notification.NotificationDTO;

import ru.skillbox.diplom.group40.social.network.api.dto.notification.Type;
import ru.skillbox.diplom.group40.social.network.domain.friend.Friend;

import java.time.ZonedDateTime;

@Mapper(componentModel = "spring")
public interface FriendMapper {

    @Mapping(target="friendId", source="friend.accountTo")
    @Mapping(target = "rating", ignore = true)
    FriendDto toDto(Friend friend);

    @Mapping(target="authorId", source="friend.accountFrom")
    @Mapping(target="receiverId", source="friend.accountTo")
    @Mapping(target="content", expression = "java(getMessage(type))")
    @Mapping(target = "notificationType", source = "type")
    @Mapping(target = "sentTime", expression = "java(getTime())")
    NotificationDTO toNotificationDTO(Friend friend, Type type);

    default String getMessage(Type type) {
        return switch (type) {
            case FRIEND_REQUEST -> "Вам предлагает дружбу";
            case FRIEND_APPROVE -> "Ваша заявка на дружбу одобрена";
            case FRIEND_SUBSCRIBE -> "На ваши обновления подписался";
            case FRIEND_BLOCKED -> "Вас заблокировал";
            case FRIEND_UNBLOCKED -> "Вас разблокировал";
            default -> "!";
        };
    }

    default ZonedDateTime getTime() {
        return ZonedDateTime.now();
    }

}
