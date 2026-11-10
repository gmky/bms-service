package io.kalenz.bms.access.control.controller;

import io.kalenz.approval.generated.api.access.control.client.v1.UserAssignmentsApi;
import io.kalenz.approval.generated.api.access.control.client.v1.model.AssignJobRoleRequest;
import io.kalenz.approval.generated.api.access.control.client.v1.model.DataGroupDto;
import io.kalenz.approval.generated.api.access.control.client.v1.model.DataGroupScopeRequest;
import io.kalenz.approval.generated.api.access.control.client.v1.model.UserAssignedJobRoleDto;
import io.kalenz.bms.access.control.service.UserAssignmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserAssignmentController implements UserAssignmentsApi {

    private final UserAssignmentService userAssignmentService;

    @Override
    public ResponseEntity<List<UserAssignedJobRoleDto>> listAssignedJobRoles(String contextId) {
        return ResponseEntity.ok(userAssignmentService.listAssignedJobRoles(contextId));
    }

    @PreAuthorize("@accessGuard.hasPrivilege('manage.entitlements', 'user-assignment', 'CREATE')")
    @Override
    public ResponseEntity<UserAssignedJobRoleDto> assignJobRole(String contextId, AssignJobRoleRequest assignJobRoleRequest) {
        return ResponseEntity.status(201).body(userAssignmentService.assignJobRole(contextId, assignJobRoleRequest));
    }

    @PreAuthorize("@accessGuard.hasPrivilege('manage.entitlements', 'user-assignment', 'DELETE')")
    @Override
    public ResponseEntity<Void> unassignJobRole(String contextId, String assignmentId) {
        userAssignmentService.unassignJobRole(contextId, assignmentId);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<List<DataGroupDto>> listAssignmentDataGroups(String assignmentId) {
        return ResponseEntity.ok(userAssignmentService.listAssignmentDataGroups(assignmentId));
    }

    @Override
    public ResponseEntity<List<DataGroupDto>> setAssignmentDataGroups(String assignmentId, DataGroupScopeRequest dataGroupScopeRequest) {
        return ResponseEntity.ok(userAssignmentService.setAssignmentDataGroups(assignmentId, dataGroupScopeRequest));
    }

    @Override
    public ResponseEntity<Void> addDataGroupToAssignment(String assignmentId, String dataGroupId) {
        userAssignmentService.addDataGroupToAssignment(assignmentId, dataGroupId);
        return ResponseEntity.status(201).build();
    }

    @Override
    public ResponseEntity<Void> removeDataGroupFromAssignment(String assignmentId, String dataGroupId) {
        userAssignmentService.removeDataGroupFromAssignment(assignmentId, dataGroupId);
        return ResponseEntity.noContent().build();
    }
}
