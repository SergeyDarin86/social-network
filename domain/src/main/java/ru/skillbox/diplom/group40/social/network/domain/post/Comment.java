package ru.skillbox.diplom.group40.social.network.domain.post;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;
import ru.skillbox.diplom.group40.social.network.api.dto.post.CommentType;
import ru.skillbox.diplom.group40.social.network.domain.base.BaseEntity;

import java.time.ZonedDateTime;
import java.util.UUID;
@Table (name = "comment")
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Comment extends BaseEntity {
    @Column (name = "comment_type")
    @Enumerated(EnumType.STRING)
    private CommentType commentType;
    @Column
    private ZonedDateTime time;
    @Column (name = "time_changed")
    private ZonedDateTime timeChanged;
    @Column (name = "author_id")
    private UUID authorId;
    @Column (name = "parent_id")
    private UUID parentId;
    @Column (name = "comment_text")
    private String commentText;
    @Column (name = "post_id")
    private UUID postId;
    @Column (name = "is_blocked")
    private Boolean isBlocked;
    @Column (name = "like_amount")
    private Integer likeAmount;
    @Column (name = "my_like")
    private Boolean myLike;
    @Column (name = "comments_count")
    private Integer commentsCount;
    @Column (name = "image_path")
    private String imagePath;
}
