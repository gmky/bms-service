package io.kalenz.bms.access.control.repository;

import io.kalenz.bms.access.control.entity.JobRole;
import io.kalenz.bms.access.control.enumeration.JobRoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface JobRoleRepository extends JpaRepository<JobRole, String> {
    Optional<JobRole> findByName(String name);

    List<JobRole> findByServiceAgreementId(String serviceAgreementId);

    List<JobRole> findByType(JobRoleType type);

    @Query("SELECT j FROM JobRole j WHERE j.serviceAgreement.id = :serviceAgreementId AND j.type = :type")
    List<JobRole> findByServiceAgreementIdAndType(
            @Param("serviceAgreementId") String serviceAgreementId,
            @Param("type") JobRoleType type);

    @Query("SELECT j FROM JobRole j WHERE j.startDate <= :now AND (j.endDate IS NULL OR j.endDate >= :now)")
    List<JobRole> findActiveRoles(@Param("now") Instant now);
}
