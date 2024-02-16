package com.andrey.entity;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.Instant;

@Getter
@Setter
@MappedSuperclass
public abstract class AuditableEntity <T extends Serializable> implements BaseEntity<T>{

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "created_by")
    private String createdBy;

    private Instant updateAt;

    private String updateBy;
//    @PrePersist
//    public void prePersist() {
//        setCreatedAt(Instant.now());
//        setCreatedBy("");
//    }
//
//    @PreUpdate
//    public void preUpdate() {
//        setCreatedAt(Instant.now());
//    }
}
