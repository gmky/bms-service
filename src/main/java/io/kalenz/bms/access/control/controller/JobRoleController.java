package io.kalenz.bms.access.control.controller;

import io.kalenz.approval.generated.api.access.control.client.v1.JobRolesApi;
import io.kalenz.approval.generated.api.access.control.client.v1.model.JobRoleDto;
import io.kalenz.approval.generated.api.access.control.client.v1.model.JobRolePrivilegesRequest;
import io.kalenz.approval.generated.api.access.control.client.v1.model.JobRoleRequest;
import io.kalenz.approval.generated.api.access.control.client.v1.model.JobRoleType;
import io.kalenz.approval.generated.api.access.control.client.v1.model.PrivilegeDto;
import io.kalenz.bms.access.control.service.JobRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class JobRoleController implements JobRolesApi {

    private final JobRoleService jobRoleService;

    @PreAuthorize("@accessGuard.hasPrivilege('manage.entitlements', 'job-role', 'VIEW')")
    @Override
    public ResponseEntity<List<JobRoleDto>> listJobRolesByServiceAgreement(String saId, JobRoleType type) {
        return ResponseEntity.ok(jobRoleService.listJobRolesByServiceAgreement(saId, type));
    }

    @PreAuthorize("@accessGuard.hasPrivilege('manage.entitlements', 'job-role', 'CREATE')")
    @Override
    public ResponseEntity<JobRoleDto> createJobRole(String saId, JobRoleRequest jobRoleRequest) {
        return ResponseEntity.status(201).body(jobRoleService.createJobRole(saId, jobRoleRequest));
    }

    @Override
    public ResponseEntity<List<JobRoleDto>> listReferenceJobRoles() {
        return ResponseEntity.ok(jobRoleService.listReferenceJobRoles());
    }

    @Override
    public ResponseEntity<JobRoleDto> getJobRole(String id) {
        return ResponseEntity.ok(jobRoleService.getJobRole(id));
    }

    @Override
    public ResponseEntity<JobRoleDto> updateJobRole(String id, JobRoleRequest jobRoleRequest) {
        return ResponseEntity.ok(jobRoleService.updateJobRole(id, jobRoleRequest));
    }

    @Override
    public ResponseEntity<Void> deleteJobRole(String id) {
        jobRoleService.deleteJobRole(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<List<PrivilegeDto>> listJobRolePrivileges(String id) {
        return ResponseEntity.ok(jobRoleService.listJobRolePrivileges(id));
    }

    @Override
    public ResponseEntity<List<PrivilegeDto>> setJobRolePrivileges(String id, JobRolePrivilegesRequest jobRolePrivilegesRequest) {
        return ResponseEntity.ok(jobRoleService.setJobRolePrivileges(id, jobRolePrivilegesRequest));
    }

    @Override
    public ResponseEntity<Void> addPrivilegeToJobRole(String id, String privilegeId) {
        jobRoleService.addPrivilegeToJobRole(id, privilegeId);
        return ResponseEntity.status(201).build();
    }

    @Override
    public ResponseEntity<Void> removePrivilegeFromJobRole(String id, String privilegeId) {
        jobRoleService.removePrivilegeFromJobRole(id, privilegeId);
        return ResponseEntity.noContent().build();
    }
}
