package io.kalenz.bms.access.control.controller;

import io.kalenz.approval.generated.api.access.control.client.v1.AccessCheckApi;
import io.kalenz.approval.generated.api.access.control.client.v1.model.AccessCheckRequest;
import io.kalenz.approval.generated.api.access.control.client.v1.model.AccessCheckResponse;
import io.kalenz.approval.generated.api.access.control.client.v1.model.BatchAccessCheckRequest;
import io.kalenz.approval.generated.api.access.control.client.v1.model.BatchAccessCheckResponse;
import io.kalenz.approval.generated.api.access.control.client.v1.model.DataGroupDto;
import io.kalenz.approval.generated.api.access.control.client.v1.model.EntitlementsResponse;
import io.kalenz.bms.access.control.service.AccessCheckService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AccessCheckController implements AccessCheckApi {

    private final AccessCheckService accessCheckService;

    @Override
    public ResponseEntity<AccessCheckResponse> checkAccess(AccessCheckRequest accessCheckRequest) {
        return ResponseEntity.ok(accessCheckService.checkAccess(accessCheckRequest));
    }

    @Override
    public ResponseEntity<BatchAccessCheckResponse> batchCheckAccess(BatchAccessCheckRequest batchAccessCheckRequest) {
        return ResponseEntity.ok(accessCheckService.batchCheckAccess(batchAccessCheckRequest));
    }

    @Override
    public ResponseEntity<EntitlementsResponse> getUserEntitlements(String userId, String serviceAgreementId) {
        return ResponseEntity.ok(accessCheckService.getUserEntitlements(userId, serviceAgreementId));
    }

    @Override
    public ResponseEntity<List<DataGroupDto>> getUserDataGroups(String userId, String serviceAgreementId, String type) {
        return ResponseEntity.ok(accessCheckService.getUserDataGroups(userId, serviceAgreementId, type));
    }
}
