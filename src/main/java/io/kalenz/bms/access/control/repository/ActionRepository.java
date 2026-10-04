package io.kalenz.bms.access.control.repository;

import io.kalenz.bms.access.control.entity.Action;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ActionRepository extends JpaRepository<Action, String> {
    Optional<Action> findByCode(String code);
}
