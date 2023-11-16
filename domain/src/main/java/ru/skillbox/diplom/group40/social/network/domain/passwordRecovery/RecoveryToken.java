package ru.skillbox.diplom.group40.social.network.domain.passwordRecovery;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.skillbox.diplom.group40.social.network.domain.base.BaseEntity;

import java.time.ZonedDateTime;
import java.util.UUID;

@Table(name = "recover_token")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RecoveryToken extends BaseEntity {

    @Column
    UUID userId;
    @Column
    ZonedDateTime expirationTime;
}
