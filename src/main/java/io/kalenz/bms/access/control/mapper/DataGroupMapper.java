package io.kalenz.bms.access.control.mapper;

import io.kalenz.approval.generated.api.access.control.client.v1.model.DataGroupDto;
import io.kalenz.approval.generated.api.access.control.client.v1.model.DataGroupItemDto;
import io.kalenz.approval.generated.api.access.control.client.v1.model.DataGroupRequest;
import io.kalenz.bms.access.control.entity.DataGroup;
import io.kalenz.bms.access.control.entity.DataGroupItem;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", uses = {InstantOffsetDateTimeMapper.class})
public interface DataGroupMapper {

    @Mapping(source = "serviceAgreement.id", target = "serviceAgreementId")
    DataGroupDto toDto(DataGroup dataGroup);

    DataGroupItemDto toDto(DataGroupItem dataGroupItem);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "serviceAgreement", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "dataGroupItems", ignore = true)
    @Mapping(target = "userAssignedDataGroups", ignore = true)
    void updateDataGroupFromRequest(@MappingTarget DataGroup target, DataGroupRequest request);
}
