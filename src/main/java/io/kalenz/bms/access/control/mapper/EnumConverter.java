package io.kalenz.bms.access.control.mapper;

import io.kalenz.approval.generated.api.access.control.client.v1.model.JobRoleType;
import io.kalenz.approval.generated.api.access.control.client.v1.model.ServiceAgreementState;
import io.kalenz.approval.generated.api.access.control.client.v1.model.UserContextState;
import io.kalenz.approval.generated.api.access.control.client.v1.model.UserContextType;
import org.springframework.stereotype.Component;

/**
 * Converts between entity enums (io.kalenz.bms.access.control.enumeration.*)
 * and generated model enums (io.kalenz.approval.generated.api.access.control.client.v1.model.*).
 *
 * <p>Both packages expose enums with identical simple names (e.g. {@code ServiceAgreementState}).
 * This class is the single place that qualifies entity enum references — all other classes
 * delegate here and use clean short imports.
 */
@Component
public class EnumConverter {

    // ── ServiceAgreementState ─────────────────────────────────────────────

    public ServiceAgreementState toModelServiceAgreementState(
            io.kalenz.bms.access.control.enumeration.ServiceAgreementState entity) {
        return entity == null ? null : ServiceAgreementState.valueOf(entity.name());
    }

    public io.kalenz.bms.access.control.enumeration.ServiceAgreementState toEntityServiceAgreementState(
            ServiceAgreementState model) {
        return model == null ? null
                : io.kalenz.bms.access.control.enumeration.ServiceAgreementState.valueOf(model.name());
    }

    // ── UserContextState ──────────────────────────────────────────────────

    public UserContextState toModelUserContextState(
            io.kalenz.bms.access.control.enumeration.UserContextState entity) {
        return entity == null ? null : UserContextState.valueOf(entity.name());
    }

    public io.kalenz.bms.access.control.enumeration.UserContextState toEntityUserContextState(
            UserContextState model) {
        return model == null ? null
                : io.kalenz.bms.access.control.enumeration.UserContextState.valueOf(model.name());
    }

    // ── UserContextType ───────────────────────────────────────────────────

    public UserContextType toModelUserContextType(
            io.kalenz.bms.access.control.enumeration.UserContextType entity) {
        return entity == null ? null : UserContextType.valueOf(entity.name());
    }

    public io.kalenz.bms.access.control.enumeration.UserContextType toEntityUserContextType(
            UserContextType model) {
        return model == null ? null
                : io.kalenz.bms.access.control.enumeration.UserContextType.valueOf(model.name());
    }

    // ── JobRoleType ───────────────────────────────────────────────────────

    public JobRoleType toModelJobRoleType(
            io.kalenz.bms.access.control.enumeration.JobRoleType entity) {
        return entity == null ? null : JobRoleType.valueOf(entity.name());
    }

    public io.kalenz.bms.access.control.enumeration.JobRoleType toEntityJobRoleType(
            JobRoleType model) {
        return model == null ? null
                : io.kalenz.bms.access.control.enumeration.JobRoleType.valueOf(model.name());
    }
}
