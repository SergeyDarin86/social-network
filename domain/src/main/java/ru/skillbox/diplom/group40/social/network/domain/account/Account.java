package ru.skillbox.diplom.group40.social.network.domain.account;

import jakarta.persistence.*;
import lombok.*;
import ru.skillbox.diplom.group40.social.network.api.dto.account.Frend;
import ru.skillbox.diplom.group40.social.network.domain.user.UserEntity;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * AccountEntity
 *
 * @taras281 Taras
 */
@Data
@Table(name = "account")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Account extends UserEntity {
    @Column
    private String phone;
    @Column
    private String photo;
    @Column
    private String profileCover;
    @Column
    private String about;
    @Column
    private String city;
    @Column
    private String country;
    @Enumerated(EnumType.STRING)
    private Frend statusCode;
    @Column
    private LocalDateTime regDate;
    @Column
    private LocalDateTime birthDate;
    @Column
    private String messagePermission;
    @Column
    private LocalDateTime lastOnlineTime;
    @Column
    private boolean isOnline;
    @Column
    private boolean isBlocked;
    @Column
    private String emojiStatus;
    @Column
    private LocalDateTime createdOn;
    @Column
    private LocalDateTime updatedOn;
    @Column
    private LocalDateTime deletionTimestamp;
}
