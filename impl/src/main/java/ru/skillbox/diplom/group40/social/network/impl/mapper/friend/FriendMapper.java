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
    @Mapping(target="content", source="friend.accountTo")
    @Mapping(target = "notificationType", expression = "java(getType())")
    NotificationDTO toNotificationDTO(Friend friend);

    default Type getType() {
        return Type.FRIEND_REQUEST;
    }

}
