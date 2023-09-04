package ru.skillbox.diplom.group40.social.network.api.dto.account;

import lombok.Data;
import ru.skillbox.diplom.group40.social.network.api.dto.base.BaseDto;

import java.time.LocalDateTime;
import java.util.UUID;

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

    //private Frend statusCode;


    private LocalDateTime birthDate;

    private String messagePermission;

    private LocalDateTime lastOnlineTime;

    private boolean isOnline;


    private boolean isBlocked;

    private String emojiStatus;

    private LocalDateTime deletionTimestamp;

    private LocalDateTime createdOn;

    private LocalDateTime updatedOn;


}
