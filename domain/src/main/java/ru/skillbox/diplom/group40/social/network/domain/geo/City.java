package ru.skillbox.diplom.group40.social.network.domain.geo;

import jakarta.persistence.*;
import lombok.*;
import ru.skillbox.diplom.group40.social.network.domain.base.BaseEntity;

@Getter
@Setter
@Entity
@Table(name = "city")
@RequiredArgsConstructor
public class City extends BaseEntity {
    @Column(name = "title")
    private String title;

    @ManyToOne
    @JoinColumn(name = "country_id")
    private Country country;

}
