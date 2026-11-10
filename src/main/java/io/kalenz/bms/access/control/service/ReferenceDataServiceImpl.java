package io.kalenz.bms.access.control.service;

import io.kalenz.approval.generated.api.access.control.client.v1.model.ActionDto;
import io.kalenz.approval.generated.api.access.control.client.v1.model.BusinessFunctionDto;
import io.kalenz.approval.generated.api.access.control.client.v1.model.BusinessFunctionRequest;
import io.kalenz.approval.generated.api.access.control.client.v1.model.PrivilegeDto;
import io.kalenz.approval.generated.api.access.control.client.v1.model.PrivilegeRequest;
import io.kalenz.approval.generated.api.access.control.client.v1.model.ResourceDto;
import io.kalenz.approval.generated.api.access.control.client.v1.model.ResourceRequest;
import io.kalenz.bms.access.control.entity.Action;
import io.kalenz.bms.access.control.entity.BusinessFunction;
import io.kalenz.bms.access.control.entity.Privilege;
import io.kalenz.bms.access.control.entity.Resource;
import io.kalenz.bms.access.control.mapper.ReferenceDataMapper;
import io.kalenz.bms.access.control.repository.ActionRepository;
import io.kalenz.bms.access.control.repository.BusinessFunctionRepository;
import io.kalenz.bms.access.control.repository.PrivilegeRepository;
import io.kalenz.bms.access.control.repository.ResourceRepository;
import io.kalenz.bms.exception.ConflictException;
import io.kalenz.bms.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ReferenceDataServiceImpl implements ReferenceDataService {

    private final ActionRepository actionRepository;
    private final ResourceRepository resourceRepository;
    private final BusinessFunctionRepository businessFunctionRepository;
    private final PrivilegeRepository privilegeRepository;
    private final ReferenceDataMapper referenceDataMapper;

    @Override
    @Transactional(readOnly = true)
    public List<ActionDto> listActions() {
        return referenceDataMapper.toActionDtoList(actionRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public ActionDto getAction(String id) {
        Action action = actionRepository.findById(id)
                .orElseThrow(() -> NotFoundException.of("Action", id));
        return referenceDataMapper.toActionDto(action);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ResourceDto> listResources() {
        return referenceDataMapper.toResourceDtoList(resourceRepository.findAll());
    }

    @Override
    public ResourceDto createResource(ResourceRequest request) {
        if (resourceRepository.findByResourceCode(request.getResourceCode()).isPresent()) {
            throw new ConflictException("Resource already exists with code: " + request.getResourceCode());
        }
        Resource resource = new Resource();
        resource.setResourceName(request.getResourceName());
        resource.setResourceCode(request.getResourceCode());
        resource.setDescription(request.getDescription());
        Resource saved = resourceRepository.save(resource);
        return referenceDataMapper.toResourceDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public ResourceDto getResource(String id) {
        Resource resource = resourceRepository.findById(id)
                .orElseThrow(() -> NotFoundException.of("Resource", id));
        return referenceDataMapper.toResourceDto(resource);
    }

    @Override
    public ResourceDto updateResource(String id, ResourceRequest request) {
        Resource resource = resourceRepository.findById(id)
                .orElseThrow(() -> NotFoundException.of("Resource", id));
        referenceDataMapper.updateResourceFromRequest(resource, request);
        Resource saved = resourceRepository.save(resource);
        return referenceDataMapper.toResourceDto(saved);
    }

    @Override
    public void deleteResource(String id) {
        Resource resource = resourceRepository.findById(id)
                .orElseThrow(() -> NotFoundException.of("Resource", id));
        resourceRepository.delete(resource);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BusinessFunctionDto> listBusinessFunctions() {
        return referenceDataMapper.toBusinessFunctionDtoList(businessFunctionRepository.findAll());
    }

    @Override
    public BusinessFunctionDto createBusinessFunction(BusinessFunctionRequest request) {
        if (businessFunctionRepository.findByFunctionCode(request.getFunctionCode()).isPresent()) {
            throw new ConflictException("BusinessFunction already exists with code: " + request.getFunctionCode());
        }
        BusinessFunction bf = new BusinessFunction();
        bf.setFunctionName(request.getFunctionName());
        bf.setFunctionCode(request.getFunctionCode());
        bf.setDescription(request.getDescription());
        BusinessFunction saved = businessFunctionRepository.save(bf);
        return referenceDataMapper.toBusinessFunctionDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public BusinessFunctionDto getBusinessFunction(String id) {
        BusinessFunction bf = businessFunctionRepository.findById(id)
                .orElseThrow(() -> NotFoundException.of("BusinessFunction", id));
        return referenceDataMapper.toBusinessFunctionDto(bf);
    }

    @Override
    public BusinessFunctionDto updateBusinessFunction(String id, BusinessFunctionRequest request) {
        BusinessFunction bf = businessFunctionRepository.findById(id)
                .orElseThrow(() -> NotFoundException.of("BusinessFunction", id));
        referenceDataMapper.updateBusinessFunctionFromRequest(bf, request);
        BusinessFunction saved = businessFunctionRepository.save(bf);
        return referenceDataMapper.toBusinessFunctionDto(saved);
    }

    @Override
    public void deleteBusinessFunction(String id) {
        BusinessFunction bf = businessFunctionRepository.findById(id)
                .orElseThrow(() -> NotFoundException.of("BusinessFunction", id));
        businessFunctionRepository.delete(bf);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PrivilegeDto> listPrivileges(String functionCode, String resourceCode, String actionCode) {
        List<Privilege> result;

        if (functionCode == null && resourceCode == null && actionCode == null) {
            result = privilegeRepository.findAll();
        } else {
            List<List<Privilege>> sets = new ArrayList<>();

            if (functionCode != null) {
                sets.add(privilegeRepository.findByFunctionCode(functionCode));
            }
            if (resourceCode != null) {
                sets.add(privilegeRepository.findByResourceCode(resourceCode));
            }
            if (actionCode != null) {
                // Filter by action code using stream since there's no findByActionCode repo method
                List<Privilege> allByAction = privilegeRepository.findAll().stream()
                        .filter(p -> p.getAction() != null && actionCode.equals(p.getAction().getCode()))
                        .collect(Collectors.toList());
                sets.add(allByAction);
            }

            if (sets.isEmpty()) {
                result = privilegeRepository.findAll();
            } else {
                // Intersect all sets
                result = new ArrayList<>(sets.get(0));
                for (int i = 1; i < sets.size(); i++) {
                    final List<Privilege> nextSet = sets.get(i);
                    result = result.stream()
                            .filter(p -> nextSet.stream().anyMatch(np -> np.getId().equals(p.getId())))
                            .collect(Collectors.toList());
                }
            }
        }

        return referenceDataMapper.toPrivilegeDtoList(result);
    }

    @Override
    public PrivilegeDto createPrivilege(PrivilegeRequest request) {
        BusinessFunction bf = businessFunctionRepository.findById(request.getFunctionId())
                .orElseThrow(() -> NotFoundException.of("BusinessFunction", request.getFunctionId()));
        Resource resource = resourceRepository.findById(request.getResourceId())
                .orElseThrow(() -> NotFoundException.of("Resource", request.getResourceId()));
        Action action = actionRepository.findById(request.getActionId())
                .orElseThrow(() -> NotFoundException.of("Action", request.getActionId()));

        Privilege privilege = new Privilege();
        privilege.setFunction(bf);
        privilege.setResource(resource);
        privilege.setAction(action);
        Privilege saved = privilegeRepository.save(privilege);
        return referenceDataMapper.toPrivilegeDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public PrivilegeDto getPrivilege(String id) {
        Privilege privilege = privilegeRepository.findById(id)
                .orElseThrow(() -> NotFoundException.of("Privilege", id));
        return referenceDataMapper.toPrivilegeDto(privilege);
    }

    @Override
    public void deletePrivilege(String id) {
        Privilege privilege = privilegeRepository.findById(id)
                .orElseThrow(() -> NotFoundException.of("Privilege", id));
        privilegeRepository.delete(privilege);
    }
}
