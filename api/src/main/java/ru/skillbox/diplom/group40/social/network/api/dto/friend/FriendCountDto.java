package ru.skillbox.diplom.group40.social.network.api.dto.friend;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FriendCountDto {

    @Schema(description = "Количество запросов в друзья")
    Integer count;
}
