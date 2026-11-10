package io.kalenz.bms.access.control.service;

import io.kalenz.approval.generated.api.access.control.client.v1.model.DataGroupDto;
import io.kalenz.approval.generated.api.access.control.client.v1.model.DataGroupScopeRequest;
import io.kalenz.approval.generated.api.access.control.client.v1.model.AssignJobRoleRequest;
import io.kalenz.approval.generated.api.access.control.client.v1.model.UserAssignedJobRoleDto;

import java.util.List;

public interface UserAssignmentService {

    List<UserAssignedJobRoleDto> listAssignedJobRoles(String contextId);

    UserAssignedJobRoleDto assignJobRole(String contextId, AssignJobRoleRequest request);

    void unassignJobRole(String contextId, String assignmentId);

    List<DataGroupDto> listAssignmentDataGroups(String assignmentId);

    List<DataGroupDto> setAssignmentDataGroups(String assignmentId, DataGroupScopeRequest request);

    void addDataGroupToAssignment(String assignmentId, String dataGroupId);

    void removeDataGroupFromAssignment(String assignmentId, String dataGroupId);
}
