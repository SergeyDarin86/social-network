package ru.skillbox.diplom.group40.social.network.impl.mapper.friend;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.skillbox.diplom.group40.social.network.api.dto.friend.FriendDto;
import ru.skillbox.diplom.group40.social.network.domain.friend.Friend;

@Mapper(componentModel = "spring")
public interface FriendMapper {

    @Mapping(target="friendId", source="friend.accountTo")
    FriendDto toDto(Friend friend);

}
