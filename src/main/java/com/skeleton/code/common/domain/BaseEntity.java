package com.skeleton.code.common.domain;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
public abstract class BaseEntity {

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @CreatedDate
    @Column(name = "reg_dt", nullable = false, updatable = false)
    protected LocalDateTime regDt;
}
