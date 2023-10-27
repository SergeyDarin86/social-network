package ru.skillbox.diplom.group40.social.network.api.dto.account;

import lombok.Data;
import ru.skillbox.diplom.group40.social.network.api.dto.base.BaseDto;
import ru.skillbox.diplom.group40.social.network.api.dto.friend.StatusCode;

import java.util.List;
import java.util.UUID;

/**
 * AccountSerchDto
 *
 * @taras281 Taras
 */
@Data
public class AccountSearchDto extends BaseDto {
    private List<UUID> ids;
    private List<UUID> blockedByIds;
    private String author;
    private String firstName;
    private String lastName;
    private String city;
    private String country;
    private String email;
    private Boolean isBlocked;
    private StatusCode statusCode;
    private Integer ageFrom;
    private Integer ageTo;


}
