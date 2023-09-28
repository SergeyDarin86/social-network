package ru.skillbox.diplom.group40.social.network.api.dto.friend;

import lombok.Data;
import ru.skillbox.diplom.group40.social.network.api.dto.base.BaseDto;

import java.util.UUID;

@Data
public class FriendDto extends BaseDto {

    private StatusCode statusCode;

    private UUID friendId;

    private StatusCode StatusCode;

    private Integer rating;

}
