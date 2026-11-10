package io.kalenz.bms.access.control.mapper;

import io.kalenz.approval.generated.api.access.control.client.v1.model.ServiceAgreementDto;
import io.kalenz.approval.generated.api.access.control.client.v1.model.ServiceAgreementRequest;
import io.kalenz.approval.generated.api.access.control.client.v1.model.ServiceAgreementState;
import io.kalenz.bms.access.control.entity.ServiceAgreement;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring", uses = {InstantOffsetDateTimeMapper.class})
public interface ServiceAgreementMapper {

    @Mapping(target = "state", source = "state")
    ServiceAgreementDto toServiceAgreementDto(ServiceAgreement serviceAgreement);

    List<ServiceAgreementDto> toServiceAgreementDtoList(List<ServiceAgreement> agreements);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "stateChangedAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "userContexts", ignore = true)
    @Mapping(target = "jobRoles", ignore = true)
    @Mapping(target = "dataGroups", ignore = true)
    @Mapping(target = "state", source = "state")
    ServiceAgreement toServiceAgreement(ServiceAgreementRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "stateChangedAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "state", ignore = true)
    @Mapping(target = "userContexts", ignore = true)
    @Mapping(target = "jobRoles", ignore = true)
    @Mapping(target = "dataGroups", ignore = true)
    void updateServiceAgreementFromRequest(@MappingTarget ServiceAgreement target, ServiceAgreementRequest request);

    /** Entity → model enum. MapStruct calls this automatically for the {@code state} field. */
    default ServiceAgreementState map(io.kalenz.bms.access.control.enumeration.ServiceAgreementState entity) {
        return entity == null ? null : ServiceAgreementState.valueOf(entity.name());
    }

    /** Model → entity enum. MapStruct calls this for the reverse mapping. */
    default io.kalenz.bms.access.control.enumeration.ServiceAgreementState map(ServiceAgreementState model) {
        return model == null ? null
                : io.kalenz.bms.access.control.enumeration.ServiceAgreementState.valueOf(model.name());
    }
}
