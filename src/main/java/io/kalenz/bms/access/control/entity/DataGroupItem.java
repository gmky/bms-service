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

@Entity
@Getter
@Setter
@Table(name = "DATA_GROUP_ITEM")
public class DataGroupItem implements Serializable {
    @Id
    @Column(name = "ID", length = 36)
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DATA_GROUP_ID", nullable = false)
    private DataGroup dataGroup;

    @Column(name = "DATA_ITEM_ID", nullable = false, length = 36)
    private String dataItemId;
}
