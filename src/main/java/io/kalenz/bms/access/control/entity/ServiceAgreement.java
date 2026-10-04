package io.kalenz.bms.access.control.entity;

import io.kalenz.bms.access.control.enumeration.ServiceAgreementState;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Table(name = "SERVICE_AGREEMENT")
public class ServiceAgreement implements Serializable {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "EXTERNAL_ID", length = 64, unique = true, nullable = false)
    private String externalId;

    @Column(name = "DESCRIPTION")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATE", length = 15, nullable = false)
    private ServiceAgreementState state;

    @Column(name = "START_DATE")
    private Instant startDate;

    @Column(name = "END_DATE")
    private Instant endDate;

    @Column(name = "STATE_CHANGED_AT")
    private Instant stateChangedAt;

    @Column(name = "CREATED_AT")
    private Instant createdAt;

    @Column(name = "UPDATED_AT")
    private Instant updatedAt;

    @OneToMany(mappedBy = "serviceAgreement", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserContext> userContexts = new ArrayList<>();

    @OneToMany(mappedBy = "serviceAgreement", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<JobRole> jobRoles = new ArrayList<>();

    @OneToMany(mappedBy = "serviceAgreement", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DataGroup> dataGroups = new ArrayList<>();
}
