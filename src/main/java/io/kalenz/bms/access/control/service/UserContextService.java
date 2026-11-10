package io.kalenz.bms.access.control.service;

import io.kalenz.approval.generated.api.access.control.client.v1.model.UserContextDto;
import io.kalenz.approval.generated.api.access.control.client.v1.model.UserContextRequest;
import io.kalenz.approval.generated.api.access.control.client.v1.model.UserContextState;
import io.kalenz.approval.generated.api.access.control.client.v1.model.UserContextStateRequest;

import java.util.List;

public interface UserContextService {

    List<UserContextDto> listUsersInServiceAgreement(String saId, UserContextState modelState);

    UserContextDto addUserToServiceAgreement(String saId, UserContextRequest request);

    List<UserContextDto> listUserContexts(String userId);

    UserContextDto getUserContext(String id);

    void removeUserFromServiceAgreement(String id);

    UserContextDto updateUserContextState(String id, UserContextStateRequest request);
}
