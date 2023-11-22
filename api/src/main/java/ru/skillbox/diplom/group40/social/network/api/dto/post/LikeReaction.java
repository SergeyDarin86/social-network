package ru.skillbox.diplom.group40.social.network.api.dto.post;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LikeReaction {
    String reactionType;
    Integer count;
}
