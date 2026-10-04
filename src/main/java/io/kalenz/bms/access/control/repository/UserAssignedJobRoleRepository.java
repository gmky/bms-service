package io.kalenz.bms.access.control.repository;

import io.kalenz.bms.access.control.entity.UserAssignedJobRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserAssignedJobRoleRepository extends JpaRepository<UserAssignedJobRole, String> {
    List<UserAssignedJobRole> findByUserContextId(String userContextId);
    List<UserAssignedJobRole> findByJobRoleId(String jobRoleId);
}
