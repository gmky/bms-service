package io.kalenz.bms.access.control.controller;

import io.kalenz.approval.generated.api.access.control.client.v1.ServiceAgreementsApi;
import io.kalenz.approval.generated.api.access.control.client.v1.model.ServiceAgreementDto;
import io.kalenz.approval.generated.api.access.control.client.v1.model.ServiceAgreementRequest;
import io.kalenz.approval.generated.api.access.control.client.v1.model.ServiceAgreementState;
import io.kalenz.approval.generated.api.access.control.client.v1.model.ServiceAgreementStateRequest;
import io.kalenz.bms.access.control.service.ServiceAgreementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ServiceAgreementController implements ServiceAgreementsApi {

    private final ServiceAgreementService serviceAgreementService;

    @Override
    public ResponseEntity<List<ServiceAgreementDto>> listServiceAgreements(ServiceAgreementState state) {
        return ResponseEntity.ok(serviceAgreementService.listServiceAgreements(state));
    }

    @Override
    public ResponseEntity<ServiceAgreementDto> createServiceAgreement(ServiceAgreementRequest serviceAgreementRequest) {
        return ResponseEntity.status(201).body(serviceAgreementService.createServiceAgreement(serviceAgreementRequest));
    }

    @Override
    public ResponseEntity<ServiceAgreementDto> getServiceAgreement(String id) {
        return ResponseEntity.ok(serviceAgreementService.getServiceAgreement(id));
    }

    @Override
    public ResponseEntity<ServiceAgreementDto> updateServiceAgreement(String id, ServiceAgreementRequest serviceAgreementRequest) {
        return ResponseEntity.ok(serviceAgreementService.updateServiceAgreement(id, serviceAgreementRequest));
    }

    @Override
    public ResponseEntity<Void> deleteServiceAgreement(String id) {
        serviceAgreementService.deleteServiceAgreement(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<ServiceAgreementDto> updateServiceAgreementState(String id, ServiceAgreementStateRequest serviceAgreementStateRequest) {
        return ResponseEntity.ok(serviceAgreementService.updateServiceAgreementState(id, serviceAgreementStateRequest));
    }
}
