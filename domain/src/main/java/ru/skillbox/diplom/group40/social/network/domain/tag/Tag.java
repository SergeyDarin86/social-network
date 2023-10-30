package ru.skillbox.diplom.group40.social.network.domain.tag;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import ru.skillbox.diplom.group40.social.network.domain.base.BaseEntity;

/**
 * TagEntity
 *
 * @author Sergey D.
 */

@Table(name = "tag")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Tag extends BaseEntity {
    @Column
    private String name;

}
