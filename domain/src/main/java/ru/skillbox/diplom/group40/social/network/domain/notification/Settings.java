package ru.skillbox.diplom.group40.social.network.domain.notification;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import ru.skillbox.diplom.group40.social.network.domain.base.BaseEntity;


import java.util.UUID;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "notification_settings")
public class Settings extends BaseEntity {

    @Column(name="account_id")
    private UUID accountId;
    @ColumnDefault("true")
    @Column(name="enable_like")
    private boolean enableLike;
    @Column(name="enable_post")
    private boolean enablePost;
    @Column(name="enable_post_comment")
    private boolean enablePostComment;
    @Column(name="enable_comment_comment")
    private boolean enableCommentComment;
    @Column(name="enable_message")
    private boolean enableMessage;
    @Column(name="enable_friend_request")
    private boolean enableFriendRequest;
    @Column(name="enable_friend_birthday")
    private boolean enableFriendBirthday;
    @Column(name="enable_send_email_message")
    private boolean enableSendEmailMessage;

    public void setAccountId(UUID uuid) {
        this.accountId = uuid;
        setIsDeleted(false);
        this.enableLike = true;
        this.enablePost = true;
        this.enablePostComment = true;
        this.enableCommentComment = true;
        this.enableMessage = true;
        this.enableFriendRequest = true;
        this.enableFriendBirthday = true;
        this.enableSendEmailMessage = true;
    }
}
