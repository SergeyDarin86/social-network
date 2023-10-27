package ru.skillbox.diplom.group40.social.network.domain.notification;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.skillbox.diplom.group40.social.network.api.dto.notification.Status;
import ru.skillbox.diplom.group40.social.network.api.dto.notification.Type;
import ru.skillbox.diplom.group40.social.network.domain.base.BaseEntity;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "event_notification")
public class EventNotification extends BaseEntity {

    @Column(name="author_id")
    UUID authorId;
    @Column(name="receiver_id")
    UUID receiverId;
    @Column(name="notification_type")
    @Enumerated(EnumType.STRING)
    Type notificationType;
    @Column(name="content")
    String content;
    @Column(name="status")
    @Enumerated(EnumType.STRING)
    Status status;

    @Override
    public String toString() {
        return "EventNotification{" +
                "authorId=" + authorId +
                ", receiverId=" + receiverId +
                ", notificationType=" + notificationType +
                ", content='" + content + '\'' +
                ", status=" + status +
                '}';
    }


}
