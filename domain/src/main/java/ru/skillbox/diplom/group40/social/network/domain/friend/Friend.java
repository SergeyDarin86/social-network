package ru.skillbox.diplom.group40.social.network.domain.friend;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.skillbox.diplom.group40.social.network.api.dto.friend.StatusCode;
import ru.skillbox.diplom.group40.social.network.domain.base.BaseEntity;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "friend")
public class Friend extends BaseEntity {

    @Column(name = "account_from", nullable = false)
    private UUID accountFrom;

    @Column(name = "account_to", nullable = false)
    private UUID accountTo;

    @Column(name = "status_Code", nullable = false)
    @Enumerated(EnumType.STRING)
    private StatusCode statusCode;

    @Column(name = "previous_status_Code")
    @Enumerated(EnumType.STRING)
    private StatusCode previousStatusCode;


}
