package io.kalenz.bms.access.control.entity;

import io.kalenz.bms.access.control.enumeration.JobRoleType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "JOB_ROLE")
public class JobRole implements Serializable {
    @Id
    @Column(name = "ID", length = 36, nullable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "NAME", unique = true, nullable = false)
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "TYPE", length = 10)
    @Enumerated(EnumType.STRING)
    private JobRoleType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SERVICE_AGREEMENT_ID")
    private ServiceAgreement serviceAgreement;

    @Column(name = "START_DATE")
    private Instant startDate;

    @Column(name = "END_DATE")
    private Instant endDate;

    @Column(name = "CREATED_AT", nullable = false)
    private Instant createdAt;

    @Column(name = "CREATED_BY", length = 50, nullable = false)
    private String createdBy;

    @Column(name = "UPDATED_AT", nullable = false)
    private Instant updatedAt;

    @Column(name = "UPDATED_BY", length = 50, nullable = false)
    private String updatedBy;

    @OneToMany(mappedBy = "jobRole", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<JobRoleItem> jobRoleItems = new ArrayList<>();

    @OneToMany(mappedBy = "jobRole", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserAssignedJobRole> userAssignedJobRoles = new ArrayList<>();
}
