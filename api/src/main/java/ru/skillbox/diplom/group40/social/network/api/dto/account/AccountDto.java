package ru.skillbox.diplom.group40.social.network.api.dto.account;

import lombok.Data;
import ru.skillbox.diplom.group40.social.network.api.dto.base.BaseDto;
import ru.skillbox.diplom.group40.social.network.api.dto.friend.StatusCode;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

/**
 * AccountDto
 *
 * @taras281 Taras
 */
@Data
public class AccountDto extends BaseDto {

    private String firstName;

    private String lastName;

    private String email;

    private String password;

    private String phone;

    private String photo;

    private String profileCover;

    private String about;

    private String city;

    private String country;
    private LocalDateTime regDate;

    private StatusCode statusCode;

    private LocalDateTime birthDate;

    private String messagePermission;

    private ZonedDateTime lastOnlineTime;

    private Boolean isOnline;

    private Boolean isBlocked;

    private String emojiStatus;

    private LocalDateTime deletionTimestamp;

    private LocalDateTime createdDate;

    private LocalDateTime lastModifiedDate;


}
