package io.kalenz.bms.access.control.repository;

import io.kalenz.bms.access.control.entity.JobRoleItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobRoleItemRepository extends JpaRepository<JobRoleItem, String> {
    List<JobRoleItem> findByJobRoleId(String jobRoleId);
    List<JobRoleItem> findByPrivilegeId(String privilegeId);
}
