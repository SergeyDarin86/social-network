package ru.skillbox.diplom.group40.social.network.domain.post;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.skillbox.diplom.group40.social.network.api.dto.post.Type;
import ru.skillbox.diplom.group40.social.network.domain.base.BaseEntity;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * PostEntity
 *
 * @author Sergey D.
 */

@Table(name = "post")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Post extends BaseEntity{

    @Column
    private LocalDateTime time;

    @Column
    private LocalDateTime timeChanged;

    @Column
    private UUID authorId;

    @Column
    private String title;

    @Column
    @Enumerated(EnumType.STRING)
    private Type type;

    @Column
    private String postText;

    @Column
    private Boolean isBlocked;

    @Column
    private Integer commentsCount;

    private String reactionType;

    @Column
    private String myReaction;

    @Column
    private Integer likeAmount;

    @Column
    private Boolean myLike;

    @Column
    private String imagePath;

    @Column
    private LocalDateTime publishDate;
}
