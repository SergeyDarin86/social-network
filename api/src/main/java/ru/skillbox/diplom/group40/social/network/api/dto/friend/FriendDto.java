package ru.skillbox.diplom.group40.social.network.api.dto.friend;


import lombok.Data;
import lombok.NoArgsConstructor;
import ru.skillbox.diplom.group40.social.network.api.dto.base.BaseDto;

import java.util.UUID;

@Data
@NoArgsConstructor
public class FriendDto extends BaseDto {
    public FriendDto(UUID friendId, Integer rating) {
        this.friendId = friendId;
        this.rating = rating;
    }

    private StatusCode statusCode;

    private UUID friendId;

    private StatusCode previousStatusCode;

    private Integer rating;

}
