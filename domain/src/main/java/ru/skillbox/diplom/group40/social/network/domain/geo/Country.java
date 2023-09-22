package ru.skillbox.diplom.group40.social.network.domain.geo;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import ru.skillbox.diplom.group40.social.network.domain.base.BaseEntity;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@RequiredArgsConstructor
@Table(name = "country")
public class Country extends BaseEntity {
    @Column(name = "title")
    private String title;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "country")
    private List<City> cities = new ArrayList<>();

}
