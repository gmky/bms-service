package io.kalenz.bms.access.control.service;

import io.kalenz.approval.generated.api.access.control.client.v1.model.JobRoleDto;
import io.kalenz.approval.generated.api.access.control.client.v1.model.JobRolePrivilegesRequest;
import io.kalenz.approval.generated.api.access.control.client.v1.model.JobRoleRequest;
import io.kalenz.approval.generated.api.access.control.client.v1.model.JobRoleType;
import io.kalenz.approval.generated.api.access.control.client.v1.model.PrivilegeDto;

import java.util.List;

public interface JobRoleService {

    List<JobRoleDto> listJobRolesByServiceAgreement(String saId, JobRoleType type);

    JobRoleDto createJobRole(String saId, JobRoleRequest request);

    List<JobRoleDto> listReferenceJobRoles();

    JobRoleDto getJobRole(String id);

    JobRoleDto updateJobRole(String id, JobRoleRequest request);

    void deleteJobRole(String id);

    List<PrivilegeDto> listJobRolePrivileges(String id);

    List<PrivilegeDto> setJobRolePrivileges(String id, JobRolePrivilegesRequest request);

    void addPrivilegeToJobRole(String id, String privilegeId);

    void removePrivilegeFromJobRole(String id, String privilegeId);
}
