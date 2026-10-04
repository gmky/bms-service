package io.kalenz.bms.access.control.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Getter
@Setter
@Table(name = "RESOURCE")
public class Resource implements Serializable {
    @Id
    @Column(name = "ID", length = 36)
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "RESOURCE_NAME", length = 100, unique = true, nullable = false)
    private String resourceName;

    @Column(name = "RESOURCE_CODE", length = 100, unique = true, nullable = false)
    private String resourceCode;

    @Column(name = "DESCRIPTION")
    private String description;
}
