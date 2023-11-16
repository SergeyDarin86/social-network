package ru.skillbox.diplom.group40.social.network.domain.captcha;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.skillbox.diplom.group40.social.network.domain.base.BaseEntity;

import java.time.ZonedDateTime;

@Table(name = "captcha")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Captcha extends BaseEntity {
    @Column
    ZonedDateTime expirationTime;
    @Column
    String answer;
}
