package io.kalenz.bms.access.control.repository;

import io.kalenz.bms.access.control.entity.Privilege;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrivilegeRepository extends JpaRepository<Privilege, String> {

    List<Privilege> findByFunctionId(String functionId);

    List<Privilege> findByResourceId(String resourceId);

    List<Privilege> findByActionId(String actionId);

    @Query("SELECT p FROM Privilege p WHERE p.function.functionCode = :functionCode")
    List<Privilege> findByFunctionCode(@Param("functionCode") String functionCode);

    @Query("SELECT p FROM Privilege p WHERE p.resource.resourceCode = :resourceCode")
    List<Privilege> findByResourceCode(@Param("resourceCode") String resourceCode);
}
