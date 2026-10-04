package io.kalenz.bms.access.control.repository;

import io.kalenz.bms.access.control.entity.ServiceAgreement;
import io.kalenz.bms.access.control.enumeration.ServiceAgreementState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ServiceAgreementRepository extends JpaRepository<ServiceAgreement, String> {
    Optional<ServiceAgreement> findByExternalId(String externalId);

    List<ServiceAgreement> findByState(ServiceAgreementState state);
}
