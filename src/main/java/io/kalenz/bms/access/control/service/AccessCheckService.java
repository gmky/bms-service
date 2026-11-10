package io.kalenz.bms.access.control.service;

import io.kalenz.approval.generated.api.access.control.client.v1.model.AccessCheckRequest;
import io.kalenz.approval.generated.api.access.control.client.v1.model.AccessCheckResponse;
import io.kalenz.approval.generated.api.access.control.client.v1.model.BatchAccessCheckRequest;
import io.kalenz.approval.generated.api.access.control.client.v1.model.BatchAccessCheckResponse;
import io.kalenz.approval.generated.api.access.control.client.v1.model.DataGroupDto;
import io.kalenz.approval.generated.api.access.control.client.v1.model.EntitlementsResponse;

import java.util.List;

public interface AccessCheckService {

    AccessCheckResponse checkAccess(AccessCheckRequest request);

    BatchAccessCheckResponse batchCheckAccess(BatchAccessCheckRequest request);

    EntitlementsResponse getUserEntitlements(String userId, String serviceAgreementId);

    List<DataGroupDto> getUserDataGroups(String userId, String serviceAgreementId, String type);

    /**
     * Checks whether a user holds a specific privilege within a service agreement.
     * This method is intended for use by {@link io.kalenz.bms.security.AccessGuard}
     * to back {@code @PreAuthorize} expressions.
     *
     * @param userId            Keycloak {@code sub} UUID of the user
     * @param serviceAgreementId service agreement ID (from the {@code x-user-context} header)
     * @param functionCode      business function code
     * @param resourceCode      resource type code
     * @param actionCode        action code
     * @return {@code true} if the user's active user context has a job role containing
     *         the matching privilege triple; {@code false} otherwise
     */
    boolean hasPrivilege(String userId, String serviceAgreementId,
                         String functionCode, String resourceCode, String actionCode);
}
