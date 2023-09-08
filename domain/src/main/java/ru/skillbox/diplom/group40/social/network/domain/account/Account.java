package ru.skillbox.diplom.group40.social.network.domain.account;

import jakarta.persistence.*;
import lombok.*;
import ru.skillbox.diplom.group40.social.network.domain.user.User;

import java.time.LocalDateTime;

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
public class Account extends User {
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
//    @Enumerated(EnumType.STRING)
//    private Frend statusCode;
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
    private LocalDateTime deletionTimestamp;
}
