package ru.skillbox.diplom.group40.social.network.api.dto.user;

import lombok.Getter;
import lombok.Setter;
import ru.skillbox.diplom.group40.social.network.api.dto.base.BaseDto;

import java.time.ZonedDateTime;

@Getter
@Setter
public class UserDto extends BaseDto {

    private String firstName;

    private String lastName;

    private String email;

    private ZonedDateTime registrationDate;

    private ZonedDateTime createdOn;

    private ZonedDateTime updatedOn;

    private String password;
}
