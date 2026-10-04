package io.kalenz.bms.access.control.repository;

import io.kalenz.bms.access.control.entity.DataGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DataGroupRepository extends JpaRepository<DataGroup, String> {
    Optional<DataGroup> findByName(String name);

    List<DataGroup> findByServiceAgreementId(String serviceAgreementId);

    List<DataGroup> findByType(String type);
}
