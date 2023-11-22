package ru.skillbox.diplom.group40.social.network.api.dto.friend;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import ru.skillbox.diplom.group40.social.network.api.dto.base.BaseDto;

import java.util.List;
import java.util.UUID;

@Data
public class FriendSearchDto extends BaseDto {

    @Schema(description = "Список id отношений")
    private List<UUID> ids;

    @Schema(description = "Список id пользователей")
    private List<UUID> friendIds;

    @Schema(description = "id пользователя исходящих запросов")
    private UUID idFrom;

    @Schema(description = "id пользователя вхходящих запросов")
    private UUID idTo;

    @Schema(description = "Статус код отношений")
    private StatusCode statusCode;

    @Schema(description = "Предыдущий статус код отношений")
    private StatusCode previousStatusCode;

    @Schema(description = "Рейтинг пользователя по количеству общих друзей")
    private Integer rating;
}
