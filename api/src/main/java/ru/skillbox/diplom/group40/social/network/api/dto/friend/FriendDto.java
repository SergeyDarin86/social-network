package ru.skillbox.diplom.group40.social.network.api.dto.friend;


import io.swagger.v3.oas.annotations.media.Schema;
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

    @Schema(description = "Статус код отношений")
    private StatusCode statusCode;

    @Schema(description = "id пользователя")
    private UUID friendId;

    @Schema(description = "Предыдущий статус код отношений")
    private StatusCode previousStatusCode;

    @Schema(description = "Рейтинг пользователя по количеству общих друзей при выводе рекомендаций дружбы")
    private Integer rating;

}
