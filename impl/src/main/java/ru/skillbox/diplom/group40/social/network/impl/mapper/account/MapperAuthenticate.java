package ru.skillbox.diplom.group40.social.network.impl.mapper.account;

import ru.skillbox.diplom.group40.social.network.api.dto.user.UserDto;
import ru.skillbox.diplom.group40.social.network.domain.user.User;

public interface MapperAuthenticate {
    UserDto toDto(User user);
}
