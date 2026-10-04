package io.kalenz.bms.access.control.repository;

import io.kalenz.bms.access.control.entity.BusinessFunction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BusinessFunctionRepository extends JpaRepository<BusinessFunction, String> {
    Optional<BusinessFunction> findByFunctionCode(String functionCode);
}
