package ru.skillbox.diplom.group40.social.network.domain.base;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Getter
@Setter
@Table
@Data
@MappedSuperclass
@Inheritance(strategy = InheritanceType.JOINED)
public class BaseEntity {
    @Id
    @Column(name = "ID", updatable = false)
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private UUID id;
    private Boolean isDeleted;
}
