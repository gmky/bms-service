package io.kalenz.bms.access.control.repository;

import io.kalenz.bms.access.control.entity.Resource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ResourceRepository extends JpaRepository<Resource, String> {
    Optional<Resource> findByResourceCode(String resourceCode);
}
