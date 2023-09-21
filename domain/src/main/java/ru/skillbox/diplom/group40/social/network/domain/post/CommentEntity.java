package ru.skillbox.diplom.group40.social.network.domain.post;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;
import ru.skillbox.diplom.group40.social.network.domain.base.BaseEntity;
import ru.skillbox.diplom.group40.social.network.domain.post.CommentType;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table (name = "comments")
@NoArgsConstructor
public class CommentEntity extends BaseEntity {
    @Column
    @Enumerated(EnumType.STRING)
    private CommentType commentType;
    @Column
    private LocalDateTime time;
    @Column
    private LocalDateTime timeChanged;
    @Column
    private UUID authorId;
    @Column
    private UUID parentId;
    @Column
    private String commentText;
    @Column
    private UUID postId;
    @Column
    private Boolean isBlocked;
    @Column
    private Integer likeAmount;
    @Column
    private Boolean myLike;
    @Column
    private Integer commentsCount;
    @Column
    private String imagePath;
}
