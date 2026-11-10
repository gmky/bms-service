package io.kalenz.bms.access.control.mapper;

import io.kalenz.approval.generated.api.access.control.client.v1.model.JobRoleDto;
import io.kalenz.approval.generated.api.access.control.client.v1.model.JobRoleRequest;
import io.kalenz.approval.generated.api.access.control.client.v1.model.JobRoleType;
import io.kalenz.bms.access.control.entity.JobRole;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", uses = {InstantOffsetDateTimeMapper.class})
public interface JobRoleMapper {

    @Mapping(source = "serviceAgreement.id", target = "serviceAgreementId")
    @Mapping(target = "type", source = "type")
    JobRoleDto toDto(JobRole jobRole);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "serviceAgreement", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "jobRoleItems", ignore = true)
    @Mapping(target = "userAssignedJobRoles", ignore = true)
    @Mapping(target = "type", ignore = true)
    void updateJobRoleFromRequest(@MappingTarget JobRole target, JobRoleRequest request);

    /**
     * MapStruct picks this up automatically when converting
     * entity {@code JobRoleType} → model {@code JobRoleType}.
     * Default methods compile in the interface's import context,
     * so short class names work here with no FQN needed.
     */
    default JobRoleType map(io.kalenz.bms.access.control.enumeration.JobRoleType entityType) {
        return entityType == null ? null : JobRoleType.valueOf(entityType.name());
    }
}
