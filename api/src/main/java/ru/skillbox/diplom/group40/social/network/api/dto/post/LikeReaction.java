package ru.skillbox.diplom.group40.social.network.api.dto.post;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "DTO для работы с реакциями лайков")
public class LikeReaction {

    @Schema(description = "Тип реакции лайка", example = "heart")
    String reactionType;

    @Schema(description = "Количество реакций в посте для каждого лайка", example = "1")
    Integer count;
}
