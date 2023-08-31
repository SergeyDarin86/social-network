package ru.skilllbox.diplom.group40.social.service.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.util.UUID;

/**
 * Person
 *
 * @author Your name
 */
@Data
@Table(name = "person")
@Entity
public class
Person {

    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "isDeleted", nullable = false)
    private Boolean isDeleted;
}
