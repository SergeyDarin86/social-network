package ru.skillbox.diplom.group40.social.network.domain.post;

import jakarta.persistence.*;
import lombok.*;

import ru.skillbox.diplom.group40.social.network.api.dto.post.LikeType;
import ru.skillbox.diplom.group40.social.network.domain.base.BaseEntity;

import java.time.ZonedDateTime;
import java.util.UUID;
@Table(name = "like")
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Like extends BaseEntity {
    @Column (name = "author_id")
    private UUID authorId;
    @Column
    private ZonedDateTime time;
    @Column (name = "item_id")
    private UUID itemId;
    @Column
    @Enumerated(EnumType.STRING)
    private LikeType type;
    @Column (name = "reaction_type")
    private String reactionType;
}
