package io.kalenz.bms.access.control.service;

import io.kalenz.approval.generated.api.access.control.client.v1.model.AssignJobRoleRequest;
import io.kalenz.approval.generated.api.access.control.client.v1.model.DataGroupDto;
import io.kalenz.approval.generated.api.access.control.client.v1.model.DataGroupScopeRequest;
import io.kalenz.approval.generated.api.access.control.client.v1.model.UserAssignedJobRoleDto;
import io.kalenz.bms.access.control.entity.DataGroup;
import io.kalenz.bms.access.control.entity.JobRole;
import io.kalenz.bms.access.control.entity.UserAssignedDataGroup;
import io.kalenz.bms.access.control.entity.UserAssignedJobRole;
import io.kalenz.bms.access.control.entity.UserContext;
import io.kalenz.bms.access.control.mapper.DataGroupMapper;
import io.kalenz.bms.access.control.mapper.UserAssignmentMapper;
import io.kalenz.bms.access.control.repository.DataGroupRepository;
import io.kalenz.bms.access.control.repository.JobRoleRepository;
import io.kalenz.bms.access.control.repository.UserAssignedDataGroupRepository;
import io.kalenz.bms.access.control.repository.UserAssignedJobRoleRepository;
import io.kalenz.bms.access.control.repository.UserContextRepository;
import io.kalenz.bms.exception.ConflictException;
import io.kalenz.bms.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UserAssignmentServiceImpl implements UserAssignmentService {

    private final UserContextRepository userContextRepository;
    private final JobRoleRepository jobRoleRepository;
    private final DataGroupRepository dataGroupRepository;
    private final UserAssignedJobRoleRepository userAssignedJobRoleRepository;
    private final UserAssignedDataGroupRepository userAssignedDataGroupRepository;
    private final UserAssignmentMapper userAssignmentMapper;
    private final DataGroupMapper dataGroupMapper;

    @Override
    public List<UserAssignedJobRoleDto> listAssignedJobRoles(String contextId) {
        userContextRepository.findById(contextId)
                .orElseThrow(() -> NotFoundException.of("UserContext", contextId));

        return userAssignedJobRoleRepository.findByUserContextId(contextId).stream()
                .map(userAssignmentMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserAssignedJobRoleDto assignJobRole(String contextId, AssignJobRoleRequest request) {
        UserContext userContext = userContextRepository.findById(contextId)
                .orElseThrow(() -> NotFoundException.of("UserContext", contextId));

        JobRole jobRole = jobRoleRepository.findById(request.getJobRoleId())
                .orElseThrow(() -> NotFoundException.of("JobRole", request.getJobRoleId()));

        // Check for duplicate assignment
        boolean alreadyAssigned = userAssignedJobRoleRepository.findByUserContextId(contextId)
                .stream()
                .anyMatch(a -> a.getJobRole().getId().equals(request.getJobRoleId()));
        if (alreadyAssigned) {
            throw new ConflictException("Job role is already assigned to this user context");
        }

        Instant now = Instant.now();
        UserAssignedJobRole assignment = new UserAssignedJobRole();
        assignment.setUserContext(userContext);
        assignment.setJobRole(jobRole);
        assignment.setCreatedAt(now);
        assignment.setCreatedBy("system");
        assignment.setUpdatedAt(now);
        assignment.setUpdatedBy("system");

        UserAssignedJobRole saved = userAssignedJobRoleRepository.save(assignment);

        // Assign data groups if provided
        List<String> dataGroupIds = request.getDataGroupIds();
        if (dataGroupIds != null && !dataGroupIds.isEmpty()) {
            for (String dgId : dataGroupIds) {
                DataGroup dg = dataGroupRepository.findById(dgId)
                        .orElseThrow(() -> NotFoundException.of("DataGroup", dgId));
                UserAssignedDataGroup uadg = new UserAssignedDataGroup();
                uadg.setUserAssignedJobRole(saved);
                uadg.setDataGroup(dg);
                uadg.setCreatedAt(now);
                uadg.setCreatedBy("system");
                uadg.setUpdatedAt(now);
                uadg.setUpdatedBy("system");
                userAssignedDataGroupRepository.save(uadg);
            }
        }

        // Reload to get populated dataGroups
        UserAssignedJobRole reloaded = userAssignedJobRoleRepository.findById(saved.getId())
                .orElse(saved);
        return userAssignmentMapper.toDto(reloaded);
    }

    @Override
    public void unassignJobRole(String contextId, String assignmentId) {
        UserAssignedJobRole assignment = userAssignedJobRoleRepository.findById(assignmentId)
                .orElseThrow(() -> NotFoundException.of("UserAssignedJobRole", assignmentId));

        if (!assignment.getUserContext().getId().equals(contextId)) {
            throw NotFoundException.of("UserAssignedJobRole", assignmentId);
        }

        userAssignedJobRoleRepository.delete(assignment);
    }

    @Override
    public List<DataGroupDto> listAssignmentDataGroups(String assignmentId) {
        userAssignedJobRoleRepository.findById(assignmentId)
                .orElseThrow(() -> NotFoundException.of("UserAssignedJobRole", assignmentId));

        return userAssignedDataGroupRepository.findByUserAssignedJobRoleId(assignmentId).stream()
                .map(uadg -> dataGroupMapper.toDto(uadg.getDataGroup()))
                .collect(Collectors.toList());
    }

    @Override
    public List<DataGroupDto> setAssignmentDataGroups(String assignmentId, DataGroupScopeRequest request) {
        UserAssignedJobRole assignment = userAssignedJobRoleRepository.findById(assignmentId)
                .orElseThrow(() -> NotFoundException.of("UserAssignedJobRole", assignmentId));

        // Delete all existing
        List<UserAssignedDataGroup> existing = userAssignedDataGroupRepository.findByUserAssignedJobRoleId(assignmentId);
        userAssignedDataGroupRepository.deleteAll(existing);

        Instant now = Instant.now();
        List<DataGroupDto> result = new ArrayList<>();
        for (String dgId : request.getDataGroupIds()) {
            DataGroup dg = dataGroupRepository.findById(dgId)
                    .orElseThrow(() -> NotFoundException.of("DataGroup", dgId));
            UserAssignedDataGroup uadg = new UserAssignedDataGroup();
            uadg.setUserAssignedJobRole(assignment);
            uadg.setDataGroup(dg);
            uadg.setCreatedAt(now);
            uadg.setCreatedBy("system");
            uadg.setUpdatedAt(now);
            uadg.setUpdatedBy("system");
            userAssignedDataGroupRepository.save(uadg);
            result.add(dataGroupMapper.toDto(dg));
        }

        return result;
    }

    @Override
    public void addDataGroupToAssignment(String assignmentId, String dataGroupId) {
        UserAssignedJobRole assignment = userAssignedJobRoleRepository.findById(assignmentId)
                .orElseThrow(() -> NotFoundException.of("UserAssignedJobRole", assignmentId));

        DataGroup dg = dataGroupRepository.findById(dataGroupId)
                .orElseThrow(() -> NotFoundException.of("DataGroup", dataGroupId));

        // Check if already assigned
        boolean alreadyExists = userAssignedDataGroupRepository.findByUserAssignedJobRoleId(assignmentId)
                .stream()
                .anyMatch(uadg -> uadg.getDataGroup().getId().equals(dataGroupId));
        if (alreadyExists) {
            throw new ConflictException("Data group is already assigned to this job role assignment");
        }

        Instant now = Instant.now();
        UserAssignedDataGroup uadg = new UserAssignedDataGroup();
        uadg.setUserAssignedJobRole(assignment);
        uadg.setDataGroup(dg);
        uadg.setCreatedAt(now);
        uadg.setCreatedBy("system");
        uadg.setUpdatedAt(now);
        uadg.setUpdatedBy("system");
        userAssignedDataGroupRepository.save(uadg);
    }

    @Override
    public void removeDataGroupFromAssignment(String assignmentId, String dataGroupId) {
        UserAssignedDataGroup toDelete = userAssignedDataGroupRepository.findByUserAssignedJobRoleId(assignmentId)
                .stream()
                .filter(uadg -> uadg.getDataGroup().getId().equals(dataGroupId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("DataGroup " + dataGroupId + " not found in assignment " + assignmentId));

        userAssignedDataGroupRepository.delete(toDelete);
    }
}
