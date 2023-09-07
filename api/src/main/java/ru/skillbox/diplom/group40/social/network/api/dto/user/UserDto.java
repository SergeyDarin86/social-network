package ru.skillbox.diplom.group40.social.network.api.dto.user;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.skillbox.diplom.group40.social.network.api.dto.base.BaseDto;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class UserDto extends BaseDto {

    private UUID id;

    private boolean isDeleted;

    private String firstName;

    private String lastName;

    private String email;

    private LocalDateTime registrationDate;

    private LocalDateTime createdOn;

    private LocalDateTime updatedOn;

    private String password;
}
