package ru.skillbox.diplom.group40.social.network.domain.dialog;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.skillbox.diplom.group40.social.network.domain.base.BaseEntity;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Dialog extends BaseEntity {
    @Column
    Integer unreadCount;
    @Column
    UUID conversationPartner1;
    @Column
    UUID conversationPartner2;
    @Column
    UUID lastMessage;
}
