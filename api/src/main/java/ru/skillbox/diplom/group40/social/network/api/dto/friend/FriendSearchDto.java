package ru.skillbox.diplom.group40.social.network.api.dto.friend;

import lombok.Data;
import ru.skillbox.diplom.group40.social.network.api.dto.base.BaseDto;

import java.util.List;
import java.util.UUID;

@Data
public class FriendSearchDto extends BaseDto {

    private List<UUID> ids;

    private List<UUID> friendIds;

    private UUID idFrom;

    private UUID idTo;

    private StatusCode statusCode;

    private StatusCode previousStatusCode;

    private Integer rating;
}
