package ru.skillbox.diplom.group40.social.network.domain.account;

import jakarta.persistence.*;
import lombok.*;
import ru.skillbox.diplom.group40.social.network.api.dto.friend.StatusCode;
import ru.skillbox.diplom.group40.social.network.domain.user.User;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

/**
 * AccountEntity
 *
 * @taras281 Taras
 */

@Table(name = "account")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Account extends User {
    @Column(name="phone")
    private String phone;

    @Column(name="photo")
    private String photo;

    @Column(name="profile_cover")
    private String profileCover;

    @Column(name="about")
    private String about;

    @Column(name="city")
    private String city;

    @Column(name="country")
    private String country;

    @Column(name="status_code")
    @Enumerated(EnumType.STRING)
    private StatusCode statusCode;

    @Column(name="birth_date")
    private LocalDateTime birthDate;

    @Column(name="message_permission")
    private String messagePermission;

    @Column(name="last_online_time")
    private LocalDateTime lastOnlineTime;

    @Column(name="is_online")
    private Boolean isOnline;

    @Column(name="is_blocked")
    private Boolean isBlocked;

    @Column(name="emoji_status")
    private String emojiStatus;

    @Column(name="deletion_timestamp")
    private LocalDateTime deletionTimestamp;
}
