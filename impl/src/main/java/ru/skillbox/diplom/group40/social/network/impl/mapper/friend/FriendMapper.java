package ru.skillbox.diplom.group40.social.network.impl.mapper.friend;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.skillbox.diplom.group40.social.network.api.dto.friend.FriendDto;
import ru.skillbox.diplom.group40.social.network.api.dto.notification.NotificationDTO;

import ru.skillbox.diplom.group40.social.network.api.dto.notification.Type;
import ru.skillbox.diplom.group40.social.network.domain.friend.Friend;

@Mapper(componentModel = "spring")
public interface FriendMapper {

    @Mapping(target="friendId", source="friend.accountTo")
    @Mapping(target = "rating", ignore = true)
    FriendDto toDto(Friend friend);

    @Mapping(target="authorId", source="friend.accountFrom")
    @Mapping(target="receiverId", source="friend.accountTo")
    @Mapping(target="content", expression = "java(getMessage(friend))")
    @Mapping(target = "notificationType", expression = "java(getType(friend))")
    NotificationDTO toNotificationDTO(Friend friend);

    default Type getType(Friend friend) {
        return switch (friend.getStatusCode()) {
            case REQUEST_TO -> Type.FRIEND_REQUEST;
            case FRIEND -> Type.FRIEND_APPROVE;
            case SUBSCRIBED -> Type.FRIEND_SUBSCRIBE;
            case NONE -> Type.FRIEND_BLOCKED;
            default -> Type.FRIEND_UNBLOCKED;
        };
    }
    default String getMessage(Friend friend) {
        return switch (friend.getStatusCode()) {
            case REQUEST_TO -> "Предлагает вам дружбу";
            case FRIEND -> "Одобрил заявку на дружбу";
            case SUBSCRIBED -> "Подписался на ваши обновления";
            case NONE -> "Заблокировал вас";
            default -> "Разблокировал вас";
        };
    }

}
