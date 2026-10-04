package io.kalenz.bms.access.control.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.Instant;

@Entity
@Getter
@Setter
@Table(name = "USER_ASSIGNED_DATA_GROUP")
public class UserAssignedDataGroup implements Serializable {
    @Id
    @Column(name = "ID", length = 36)
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ASSIGNED_JOB_ROLE_ID", nullable = false)
    private UserAssignedJobRole userAssignedJobRole;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DATA_GROUP_ID", nullable = false)
    private DataGroup dataGroup;

    @Column(name = "CREATED_AT", nullable = false)
    private Instant createdAt;

    @Column(name = "CREATED_BY", length = 50, nullable = false)
    private String createdBy;

    @Column(name = "UPDATED_AT", nullable = false)
    private Instant updatedAt;

    @Column(name = "UPDATED_BY", length = 50, nullable = false)
    private String updatedBy;
}
