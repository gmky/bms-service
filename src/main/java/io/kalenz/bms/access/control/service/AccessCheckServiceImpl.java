package io.kalenz.bms.access.control.service;

import io.kalenz.approval.generated.api.access.control.client.v1.model.AccessCheckRequest;
import io.kalenz.approval.generated.api.access.control.client.v1.model.AccessCheckResponse;
import io.kalenz.approval.generated.api.access.control.client.v1.model.BatchAccessCheckRequest;
import io.kalenz.approval.generated.api.access.control.client.v1.model.BatchAccessCheckResponse;
import io.kalenz.approval.generated.api.access.control.client.v1.model.BatchCheckItem;
import io.kalenz.approval.generated.api.access.control.client.v1.model.BatchCheckResultItem;
import io.kalenz.approval.generated.api.access.control.client.v1.model.DataGroupDto;
import io.kalenz.approval.generated.api.access.control.client.v1.model.EntitlementsResponse;
import io.kalenz.approval.generated.api.access.control.client.v1.model.FunctionPrivilege;
import io.kalenz.bms.access.control.entity.DataGroup;
import io.kalenz.bms.access.control.entity.JobRoleItem;
import io.kalenz.bms.access.control.entity.UserAssignedDataGroup;
import io.kalenz.bms.access.control.entity.UserAssignedJobRole;
import io.kalenz.bms.access.control.entity.UserContext;
import io.kalenz.bms.access.control.enumeration.UserContextState;
import io.kalenz.bms.access.control.mapper.DataGroupMapper;
import io.kalenz.bms.access.control.repository.JobRoleItemRepository;
import io.kalenz.bms.access.control.repository.UserAssignedDataGroupRepository;
import io.kalenz.bms.access.control.repository.UserAssignedJobRoleRepository;
import io.kalenz.bms.access.control.repository.UserContextRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AccessCheckServiceImpl implements AccessCheckService {

    private final UserContextRepository userContextRepository;
    private final UserAssignedJobRoleRepository userAssignedJobRoleRepository;
    private final UserAssignedDataGroupRepository userAssignedDataGroupRepository;
    private final JobRoleItemRepository jobRoleItemRepository;
    private final DataGroupMapper dataGroupMapper;

    @Override
    public AccessCheckResponse checkAccess(AccessCheckRequest request) {
        return performCheck(
                request.getUserId(),
                request.getServiceAgreementId(),
                request.getFunctionCode(),
                request.getResourceCode(),
                request.getActionCode()
        );
    }

    private AccessCheckResponse performCheck(String userId, String saId, String functionCode, String resourceCode, String actionCode) {
        Optional<UserContext> contextOpt = userContextRepository.findByUserIdAndServiceAgreementId(userId, saId);

        if (contextOpt.isEmpty()) {
            return new AccessCheckResponse().allowed(false).dataGroupIds(new ArrayList<>());
        }

        UserContext context = contextOpt.get();
        if (context.getState() != UserContextState.ENABLED) {
            return new AccessCheckResponse().allowed(false).dataGroupIds(new ArrayList<>());
        }

        List<UserAssignedJobRole> assignments = userAssignedJobRoleRepository.findByUserContextId(context.getId());

        for (UserAssignedJobRole assignment : assignments) {
            List<JobRoleItem> items = jobRoleItemRepository.findByJobRoleId(assignment.getJobRole().getId());
            boolean hasPrivilege = items.stream().anyMatch(item -> {
                var privilege = item.getPrivilege();
                return privilege.getFunction().getFunctionCode().equals(functionCode)
                        && privilege.getResource().getResourceCode().equals(resourceCode)
                        && privilege.getAction().getCode().equals(actionCode);
            });

            if (hasPrivilege) {
                List<String> dataGroupIds = userAssignedDataGroupRepository.findByUserAssignedJobRoleId(assignment.getId())
                        .stream()
                        .map(uadg -> uadg.getDataGroup().getId())
                        .collect(Collectors.toList());
                return new AccessCheckResponse().allowed(true).dataGroupIds(dataGroupIds);
            }
        }

        return new AccessCheckResponse().allowed(false).dataGroupIds(new ArrayList<>());
    }

    @Override
    public BatchAccessCheckResponse batchCheckAccess(BatchAccessCheckRequest request) {
        List<BatchCheckResultItem> results = new ArrayList<>();

        for (BatchCheckItem check : request.getChecks()) {
            AccessCheckResponse response = performCheck(
                    request.getUserId(),
                    request.getServiceAgreementId(),
                    check.getFunctionCode(),
                    check.getResourceCode(),
                    check.getActionCode()
            );

            BatchCheckResultItem resultItem = new BatchCheckResultItem()
                    .functionCode(check.getFunctionCode())
                    .resourceCode(check.getResourceCode())
                    .actionCode(check.getActionCode())
                    .allowed(response.getAllowed())
                    .dataGroupIds(response.getDataGroupIds());

            results.add(resultItem);
        }

        return new BatchAccessCheckResponse().results(results);
    }

    @Override
    public EntitlementsResponse getUserEntitlements(String userId, String serviceAgreementId) {
        List<UserContext> contexts;
        if (serviceAgreementId != null) {
            contexts = userContextRepository.findByUserIdAndServiceAgreementId(userId, serviceAgreementId)
                    .map(List::of)
                    .orElse(List.of());
        } else {
            contexts = userContextRepository.findByUserId(userId);
        }

        // Map (functionCode, resourceCode) -> Set<actionCode>
        Map<String, List<String>> privilegeMap = new LinkedHashMap<>();

        for (UserContext context : contexts) {
            List<UserAssignedJobRole> assignments = userAssignedJobRoleRepository.findByUserContextId(context.getId());
            for (UserAssignedJobRole assignment : assignments) {
                List<JobRoleItem> items = jobRoleItemRepository.findByJobRoleId(assignment.getJobRole().getId());
                for (JobRoleItem item : items) {
                    var privilege = item.getPrivilege();
                    String key = privilege.getFunction().getFunctionCode() + "||" + privilege.getResource().getResourceCode();
                    privilegeMap.computeIfAbsent(key, k -> new ArrayList<>())
                            .add(privilege.getAction().getCode());
                }
            }
        }

        List<FunctionPrivilege> privileges = privilegeMap.entrySet().stream()
                .map(entry -> {
                    String[] parts = entry.getKey().split("\\|\\|", 2);
                    return new FunctionPrivilege()
                            .functionCode(parts[0])
                            .resourceCode(parts[1])
                            .actions(entry.getValue().stream().distinct().collect(Collectors.toList()));
                })
                .collect(Collectors.toList());

        return new EntitlementsResponse()
                .userId(userId)
                .serviceAgreementId(serviceAgreementId)
                .privileges(privileges);
    }

    @Override
    public boolean hasPrivilege(String userId, String serviceAgreementId,
                                String functionCode, String resourceCode, String actionCode) {
        return Boolean.TRUE.equals(
                performCheck(userId, serviceAgreementId, functionCode, resourceCode, actionCode).getAllowed()
        );
    }

    @Override
    public List<DataGroupDto> getUserDataGroups(String userId, String serviceAgreementId, String type) {
        List<UserContext> contexts;
        if (serviceAgreementId != null) {
            contexts = userContextRepository.findByUserIdAndServiceAgreementId(userId, serviceAgreementId)
                    .map(List::of)
                    .orElse(List.of());
        } else {
            contexts = userContextRepository.findByUserId(userId);
        }

        Map<String, DataGroup> uniqueDataGroups = new LinkedHashMap<>();

        for (UserContext context : contexts) {
            List<UserAssignedJobRole> assignments = userAssignedJobRoleRepository.findByUserContextId(context.getId());
            for (UserAssignedJobRole assignment : assignments) {
                List<UserAssignedDataGroup> uadgs = userAssignedDataGroupRepository.findByUserAssignedJobRoleId(assignment.getId());
                for (UserAssignedDataGroup uadg : uadgs) {
                    DataGroup dg = uadg.getDataGroup();
                    uniqueDataGroups.put(dg.getId(), dg);
                }
            }
        }

        return uniqueDataGroups.values().stream()
                .filter(dg -> type == null || type.equals(dg.getType()))
                .map(dataGroupMapper::toDto)
                .collect(Collectors.toList());
    }
}
