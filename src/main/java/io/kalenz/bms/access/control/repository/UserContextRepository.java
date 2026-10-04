package io.kalenz.bms.access.control.repository;

import io.kalenz.bms.access.control.entity.UserContext;
import io.kalenz.bms.access.control.enumeration.UserContextState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserContextRepository extends JpaRepository<UserContext, String> {

    List<UserContext> findByUserId(String userId);

    List<UserContext> findByServiceAgreementId(String serviceAgreementId);

    Optional<UserContext> findByUserIdAndServiceAgreementId(String userId, String serviceAgreementId);

    List<UserContext> findByState(UserContextState state);
}
