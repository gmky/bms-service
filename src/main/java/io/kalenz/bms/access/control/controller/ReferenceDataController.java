package io.kalenz.bms.access.control.controller;

import io.kalenz.approval.generated.api.access.control.client.v1.ReferenceDataApi;
import io.kalenz.approval.generated.api.access.control.client.v1.model.ActionDto;
import io.kalenz.approval.generated.api.access.control.client.v1.model.BusinessFunctionDto;
import io.kalenz.approval.generated.api.access.control.client.v1.model.BusinessFunctionRequest;
import io.kalenz.approval.generated.api.access.control.client.v1.model.PrivilegeDto;
import io.kalenz.approval.generated.api.access.control.client.v1.model.PrivilegeRequest;
import io.kalenz.approval.generated.api.access.control.client.v1.model.ResourceDto;
import io.kalenz.approval.generated.api.access.control.client.v1.model.ResourceRequest;
import io.kalenz.bms.access.control.service.ReferenceDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ReferenceDataController implements ReferenceDataApi {

    private final ReferenceDataService referenceDataService;

    @Override
    public ResponseEntity<List<ActionDto>> listActions() {
        return ResponseEntity.ok(referenceDataService.listActions());
    }

    @Override
    public ResponseEntity<ActionDto> getAction(String id) {
        return ResponseEntity.ok(referenceDataService.getAction(id));
    }

    @Override
    public ResponseEntity<List<ResourceDto>> listResources() {
        return ResponseEntity.ok(referenceDataService.listResources());
    }

    @Override
    public ResponseEntity<ResourceDto> createResource(ResourceRequest resourceRequest) {
        return ResponseEntity.status(201).body(referenceDataService.createResource(resourceRequest));
    }

    @Override
    public ResponseEntity<ResourceDto> getResource(String id) {
        return ResponseEntity.ok(referenceDataService.getResource(id));
    }

    @Override
    public ResponseEntity<ResourceDto> updateResource(String id, ResourceRequest resourceRequest) {
        return ResponseEntity.ok(referenceDataService.updateResource(id, resourceRequest));
    }

    @Override
    public ResponseEntity<Void> deleteResource(String id) {
        referenceDataService.deleteResource(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("@accessGuard.hasPrivilege('manage.entitlements', 'reference-data', 'VIEW')")
    @Override
    public ResponseEntity<List<BusinessFunctionDto>> listBusinessFunctions() {
        return ResponseEntity.ok(referenceDataService.listBusinessFunctions());
    }

    @PreAuthorize("@accessGuard.hasPrivilege('manage.entitlements', 'reference-data', 'CREATE')")
    @Override
    public ResponseEntity<BusinessFunctionDto> createBusinessFunction(BusinessFunctionRequest businessFunctionRequest) {
        return ResponseEntity.status(201).body(referenceDataService.createBusinessFunction(businessFunctionRequest));
    }

    @Override
    public ResponseEntity<BusinessFunctionDto> getBusinessFunction(String id) {
        return ResponseEntity.ok(referenceDataService.getBusinessFunction(id));
    }

    @Override
    public ResponseEntity<BusinessFunctionDto> updateBusinessFunction(String id, BusinessFunctionRequest businessFunctionRequest) {
        return ResponseEntity.ok(referenceDataService.updateBusinessFunction(id, businessFunctionRequest));
    }

    @Override
    public ResponseEntity<Void> deleteBusinessFunction(String id) {
        referenceDataService.deleteBusinessFunction(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<List<PrivilegeDto>> listPrivileges(String functionCode, String resourceCode, String actionCode) {
        return ResponseEntity.ok(referenceDataService.listPrivileges(functionCode, resourceCode, actionCode));
    }

    @Override
    public ResponseEntity<PrivilegeDto> createPrivilege(PrivilegeRequest privilegeRequest) {
        return ResponseEntity.status(201).body(referenceDataService.createPrivilege(privilegeRequest));
    }

    @Override
    public ResponseEntity<PrivilegeDto> getPrivilege(String id) {
        return ResponseEntity.ok(referenceDataService.getPrivilege(id));
    }

    @Override
    public ResponseEntity<Void> deletePrivilege(String id) {
        referenceDataService.deletePrivilege(id);
        return ResponseEntity.noContent().build();
    }
}
