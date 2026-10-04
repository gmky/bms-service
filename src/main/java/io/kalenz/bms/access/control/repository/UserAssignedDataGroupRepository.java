package io.kalenz.bms.access.control.repository;

import io.kalenz.bms.access.control.entity.UserAssignedDataGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserAssignedDataGroupRepository extends JpaRepository<UserAssignedDataGroup, String> {
    List<UserAssignedDataGroup> findByUserAssignedJobRoleId(String userAssignedJobRoleId);
    List<UserAssignedDataGroup> findByDataGroupId(String dataGroupId);
}
