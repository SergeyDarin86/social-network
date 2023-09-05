package ru.skillbox.diplom.group40.social.network.api.dto.user;

import lombok.Data;
import ru.skillbox.diplom.group40.social.network.api.dto.base.BaseDto;

import java.util.UUID;

@Data
public class UserDto extends BaseDto {

    private UUID id;

    private boolean isDeleted;

    private String firstName;

    private String lastName;

    private String email;

    private String password;
}
