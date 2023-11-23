package ru.skillbox.diplom.group40.social.network.domain.notification;

import io.swagger.v3.oas.annotations.media.Schema;
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

    @Schema(description = "id пользователя")
    @Column(name="account_id")
    private UUID accountId;

    @Schema(description = "Поле статуса на получение пользователем уведомления типа LIKE")
    @ColumnDefault("true")
    @Column(name="enable_like")
    private boolean enableLike;

    @Schema(description = "Поле статуса на получение пользователем уведомления типа POST")
    @Column(name="enable_post")
    private boolean enablePost;

    @Schema(description = "Поле статуса на получение пользователем уведомления типа POST_COMMENT")
    @Column(name="enable_post_comment")
    private boolean enablePostComment;

    @Schema(description = "Поле статуса на получение пользователем уведомления типа COMMENT_COMMENT")
    @Column(name="enable_comment_comment")
    private boolean enableCommentComment;

    @Schema(description = "Поле статуса на получение пользователем уведомления типа MESSAGE")
    @Column(name="enable_message")
    private boolean enableMessage;

    @Schema(description = "Поле статуса на получение пользователем уведомления типа FRIEND_REQUEST")
    @Column(name="enable_friend_request")
    private boolean enableFriendRequest;

    @Schema(description = "Поле статуса на получение пользователем уведомления типа FRIEND_BIRTHDAY")
    @Column(name="enable_friend_birthday")
    private boolean enableFriendBirthday;

    @Schema(description = "Поле статуса на получение пользователем уведомления типа SEND_EMAIL_MESSAGE")
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
