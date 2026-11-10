package io.kalenz.bms.access.control.service;

import io.kalenz.approval.generated.api.access.control.client.v1.model.JobRoleDto;
import io.kalenz.approval.generated.api.access.control.client.v1.model.JobRolePrivilegesRequest;
import io.kalenz.approval.generated.api.access.control.client.v1.model.JobRoleRequest;
import io.kalenz.approval.generated.api.access.control.client.v1.model.JobRoleType;
import io.kalenz.approval.generated.api.access.control.client.v1.model.PrivilegeDto;
import io.kalenz.bms.access.control.entity.JobRole;
import io.kalenz.bms.access.control.entity.JobRoleItem;
import io.kalenz.bms.access.control.entity.Privilege;
import io.kalenz.bms.access.control.entity.ServiceAgreement;
import io.kalenz.bms.access.control.mapper.EnumConverter;
import io.kalenz.bms.access.control.mapper.JobRoleMapper;
import io.kalenz.bms.access.control.mapper.ReferenceDataMapper;
import io.kalenz.bms.access.control.repository.JobRoleItemRepository;
import io.kalenz.bms.access.control.repository.JobRoleRepository;
import io.kalenz.bms.access.control.repository.PrivilegeRepository;
import io.kalenz.bms.access.control.repository.ServiceAgreementRepository;
import io.kalenz.bms.exception.ConflictException;
import io.kalenz.bms.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class JobRoleServiceImpl implements JobRoleService {

    private final JobRoleRepository jobRoleRepository;
    private final JobRoleItemRepository jobRoleItemRepository;
    private final PrivilegeRepository privilegeRepository;
    private final ServiceAgreementRepository serviceAgreementRepository;
    private final JobRoleMapper jobRoleMapper;
    private final ReferenceDataMapper privilegeMapper;
    private final EnumConverter enumConverter;

    @Override
    @Transactional(readOnly = true)
    public List<JobRoleDto> listJobRolesByServiceAgreement(String saId, JobRoleType type) {
        if (!serviceAgreementRepository.existsById(saId)) {
            throw NotFoundException.of("ServiceAgreement", saId);
        }
        if (type == null) {
            return jobRoleRepository.findByServiceAgreementId(saId).stream()
                    .map(jobRoleMapper::toDto)
                    .toList();
        }
        return jobRoleRepository.findByServiceAgreementIdAndType(saId, enumConverter.toEntityJobRoleType(type)).stream()
                .map(jobRoleMapper::toDto)
                .toList();
    }

    @Override
    public JobRoleDto createJobRole(String saId, JobRoleRequest request) {
        ServiceAgreement serviceAgreement = serviceAgreementRepository.findById(saId)
                .orElseThrow(() -> NotFoundException.of("ServiceAgreement", saId));

        jobRoleRepository.findByName(request.getName()).ifPresent(existing -> {
            throw new ConflictException("JobRole already exists with name: " + request.getName());
        });

        JobRole jobRole = new JobRole();
        jobRole.setName(request.getName());
        jobRole.setDescription(request.getDescription());
        jobRole.setServiceAgreement(serviceAgreement);

        if (request.getType() != null) {
            jobRole.setType(enumConverter.toEntityJobRoleType(request.getType()));
        }
        if (request.getStartDate() != null) {
            jobRole.setStartDate(request.getStartDate().toInstant());
        }
        if (request.getEndDate() != null) {
            jobRole.setEndDate(request.getEndDate().toInstant());
        }

        Instant now = Instant.now();
        jobRole.setCreatedAt(now);
        jobRole.setCreatedBy("system");
        jobRole.setUpdatedAt(now);
        jobRole.setUpdatedBy("system");

        return jobRoleMapper.toDto(jobRoleRepository.save(jobRole));
    }

    @Override
    @Transactional(readOnly = true)
    public List<JobRoleDto> listReferenceJobRoles() {
        return jobRoleRepository.findByType(enumConverter.toEntityJobRoleType(JobRoleType.REFERENCE)).stream()
                .map(jobRoleMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public JobRoleDto getJobRole(String id) {
        return jobRoleMapper.toDto(jobRoleRepository.findById(id)
                .orElseThrow(() -> NotFoundException.of("JobRole", id)));
    }

    @Override
    public JobRoleDto updateJobRole(String id, JobRoleRequest request) {
        JobRole jobRole = jobRoleRepository.findById(id)
                .orElseThrow(() -> NotFoundException.of("JobRole", id));

        jobRoleMapper.updateJobRoleFromRequest(jobRole, request);

        if (request.getType() != null) {
            jobRole.setType(enumConverter.toEntityJobRoleType(request.getType()));
        }
        if (request.getStartDate() != null) {
            jobRole.setStartDate(request.getStartDate().toInstant());
        }
        if (request.getEndDate() != null) {
            jobRole.setEndDate(request.getEndDate().toInstant());
        }

        jobRole.setUpdatedAt(Instant.now());
        jobRole.setUpdatedBy("system");

        return jobRoleMapper.toDto(jobRoleRepository.save(jobRole));
    }

    @Override
    public void deleteJobRole(String id) {
        JobRole jobRole = jobRoleRepository.findById(id)
                .orElseThrow(() -> NotFoundException.of("JobRole", id));
        jobRoleRepository.delete(jobRole);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PrivilegeDto> listJobRolePrivileges(String id) {
        if (!jobRoleRepository.existsById(id)) {
            throw NotFoundException.of("JobRole", id);
        }
        return jobRoleItemRepository.findByJobRoleId(id).stream()
                .map(item -> privilegeMapper.toPrivilegeDto(item.getPrivilege()))
                .toList();
    }

    @Override
    public List<PrivilegeDto> setJobRolePrivileges(String id, JobRolePrivilegesRequest request) {
        JobRole jobRole = jobRoleRepository.findById(id)
                .orElseThrow(() -> NotFoundException.of("JobRole", id));

        jobRoleItemRepository.deleteAll(jobRoleItemRepository.findByJobRoleId(id));

        List<JobRoleItem> newItems = request.getPrivilegeIds().stream().map(privilegeId -> {
            Privilege privilege = privilegeRepository.findById(privilegeId)
                    .orElseThrow(() -> NotFoundException.of("Privilege", privilegeId));
            JobRoleItem item = new JobRoleItem();
            item.setJobRole(jobRole);
            item.setPrivilege(privilege);
            return item;
        }).toList();

        jobRoleItemRepository.saveAll(newItems);

        return jobRoleItemRepository.findByJobRoleId(id).stream()
                .map(item -> privilegeMapper.toPrivilegeDto(item.getPrivilege()))
                .toList();
    }

    @Override
    public void addPrivilegeToJobRole(String id, String privilegeId) {
        JobRole jobRole = jobRoleRepository.findById(id)
                .orElseThrow(() -> NotFoundException.of("JobRole", id));

        Privilege privilege = privilegeRepository.findById(privilegeId)
                .orElseThrow(() -> NotFoundException.of("Privilege", privilegeId));

        boolean alreadyExists = jobRoleItemRepository.findByJobRoleId(id).stream()
                .anyMatch(item -> item.getPrivilege().getId().equals(privilegeId));
        if (alreadyExists) {
            throw new ConflictException("Privilege " + privilegeId + " already assigned to job role " + id);
        }

        JobRoleItem item = new JobRoleItem();
        item.setJobRole(jobRole);
        item.setPrivilege(privilege);
        jobRoleItemRepository.save(item);
    }

    @Override
    public void removePrivilegeFromJobRole(String id, String privilegeId) {
        JobRoleItem item = jobRoleItemRepository.findByJobRoleId(id).stream()
                .filter(i -> i.getPrivilege().getId().equals(privilegeId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Privilege " + privilegeId + " not found in job role " + id));
        jobRoleItemRepository.delete(item);
    }
}
