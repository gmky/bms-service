package io.kalenz.bms.access.control.controller;

import io.kalenz.approval.generated.api.access.control.client.v1.UserContextsApi;
import io.kalenz.approval.generated.api.access.control.client.v1.model.UserContextDto;
import io.kalenz.approval.generated.api.access.control.client.v1.model.UserContextRequest;
import io.kalenz.approval.generated.api.access.control.client.v1.model.UserContextState;
import io.kalenz.approval.generated.api.access.control.client.v1.model.UserContextStateRequest;
import io.kalenz.bms.access.control.service.UserContextService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserContextController implements UserContextsApi {

    private final UserContextService userContextService;

    @Override
    public ResponseEntity<List<UserContextDto>> listUsersInServiceAgreement(String saId, UserContextState state) {
        return ResponseEntity.ok(userContextService.listUsersInServiceAgreement(saId, state));
    }

    @Override
    public ResponseEntity<UserContextDto> addUserToServiceAgreement(String saId, UserContextRequest userContextRequest) {
        return ResponseEntity.status(201).body(userContextService.addUserToServiceAgreement(saId, userContextRequest));
    }

    @Override
    public ResponseEntity<List<UserContextDto>> listUserContexts(String userId) {
        return ResponseEntity.ok(userContextService.listUserContexts(userId));
    }

    @Override
    public ResponseEntity<UserContextDto> getUserContext(String id) {
        return ResponseEntity.ok(userContextService.getUserContext(id));
    }

    @Override
    public ResponseEntity<Void> removeUserFromServiceAgreement(String id) {
        userContextService.removeUserFromServiceAgreement(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<UserContextDto> updateUserContextState(String id, UserContextStateRequest userContextStateRequest) {
        return ResponseEntity.ok(userContextService.updateUserContextState(id, userContextStateRequest));
    }
}
