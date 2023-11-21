package ru.skillbox.diplom.group40.social.network.domain.base.audit;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import ru.skillbox.diplom.group40.social.network.domain.base.BaseEntity;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;


@Getter
@Setter
@MappedSuperclass
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class BaseAuditedEntity extends BaseEntity {


    @CreatedDate
    @Column(name = "CREATED_DATE", updatable = false)
    LocalDateTime createdDate;

    @CreatedBy
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "CREATED_BY", updatable = false)
    UserJsonType createdBy;

    @LastModifiedDate
    @Column(name = "LAST_MODIFIED_DATE")
    LocalDateTime lastModifiedDate;

    @LastModifiedBy
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "LAST_MODIFIED_BY")
    UserJsonType lastModifiedBy;

}
