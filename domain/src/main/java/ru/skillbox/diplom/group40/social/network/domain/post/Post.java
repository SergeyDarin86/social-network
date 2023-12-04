package ru.skillbox.diplom.group40.social.network.domain.post;

import jakarta.persistence.*;
import lombok.*;
import ru.skillbox.diplom.group40.social.network.api.dto.post.Type;
import ru.skillbox.diplom.group40.social.network.domain.base.BaseEntity;
import ru.skillbox.diplom.group40.social.network.domain.tag.Tag;

import java.time.ZonedDateTime;
import java.util.*;

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
public class Post extends BaseEntity {

    @Column
    private ZonedDateTime time;

    @Column
    private ZonedDateTime timeChanged;

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

    @Column
    private Integer likeAmount;

    @Column
    private String imagePath;

    @Column
    private ZonedDateTime publishDate;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH}, fetch = FetchType.EAGER)
    @JoinTable(
            name = "post_tag",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )

    private List<Tag> tags = new ArrayList<>();

}
