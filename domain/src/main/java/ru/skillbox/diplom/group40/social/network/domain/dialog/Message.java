package ru.skillbox.diplom.group40.social.network.domain.dialog;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.skillbox.diplom.group40.social.network.api.dto.dialog.ReadStatus;
import ru.skillbox.diplom.group40.social.network.domain.base.BaseEntity;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Message extends BaseEntity {
    @Column
    LocalDateTime time;
    @Column
    UUID conversationPartner1;
    @Column
    UUID conversationPartner2;
    @Column
    String messageText;
    @Column
    @Enumerated(EnumType.STRING)
    ReadStatus readStatus;
    @Column
    UUID dialogId;
}
