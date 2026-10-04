package io.kalenz.bms.access.control.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.io.Serializable;

@Entity
@Table(name = "BUSINESS_FUNCTION")
public class BusinessFunction implements Serializable {
    @Id
    @Column(name = "ID", length = 36, nullable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "FUNCTION_NAME", unique = true, nullable = false, length = 100)
    private String functionName;

    @Column(name = "FUNCTION_CODE", unique = true, nullable = false, length = 100)
    private String functionCode;

    @Column(name = "DESCRIPTION")
    private String description;
}
