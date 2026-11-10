package io.kalenz.bms.access.control.mapper;

import io.kalenz.approval.generated.api.access.control.client.v1.model.DataGroupDto;
import io.kalenz.approval.generated.api.access.control.client.v1.model.UserAssignedJobRoleDto;
import io.kalenz.bms.access.control.entity.DataGroup;
import io.kalenz.bms.access.control.entity.UserAssignedDataGroup;
import io.kalenz.bms.access.control.entity.UserAssignedJobRole;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {JobRoleMapper.class, InstantOffsetDateTimeMapper.class})
public interface UserAssignmentMapper {

    @Mapping(source = "userContext.id", target = "userContextId")
    @Mapping(source = "jobRole", target = "jobRole")
    @Mapping(source = "userAssignedDataGroups", target = "dataGroups")
    UserAssignedJobRoleDto toDto(UserAssignedJobRole userAssignedJobRole);

    @Mapping(source = "serviceAgreement.id", target = "serviceAgreementId")
    DataGroupDto toDataGroupDto(DataGroup dataGroup);

    default List<DataGroupDto> mapDataGroups(List<UserAssignedDataGroup> list) {
        if (list == null) return null;
        return list.stream()
                .map(item -> toDataGroupDto(item.getDataGroup()))
                .collect(Collectors.toList());
    }
}
