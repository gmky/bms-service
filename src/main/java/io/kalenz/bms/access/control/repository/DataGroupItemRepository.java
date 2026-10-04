package io.kalenz.bms.access.control.repository;

import io.kalenz.bms.access.control.entity.DataGroupItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DataGroupItemRepository extends JpaRepository<DataGroupItem, String> {
    List<DataGroupItem> findByDataGroupId(String dataGroupId);
    List<DataGroupItem> findByDataItemId(String dataItemId);
}
