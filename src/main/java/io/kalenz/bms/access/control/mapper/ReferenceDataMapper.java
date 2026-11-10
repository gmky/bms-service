package io.kalenz.bms.access.control.mapper;

import io.kalenz.approval.generated.api.access.control.client.v1.model.ActionDto;
import io.kalenz.approval.generated.api.access.control.client.v1.model.BusinessFunctionDto;
import io.kalenz.approval.generated.api.access.control.client.v1.model.BusinessFunctionRequest;
import io.kalenz.approval.generated.api.access.control.client.v1.model.PrivilegeDto;
import io.kalenz.approval.generated.api.access.control.client.v1.model.ResourceDto;
import io.kalenz.approval.generated.api.access.control.client.v1.model.ResourceRequest;
import io.kalenz.bms.access.control.entity.Action;
import io.kalenz.bms.access.control.entity.BusinessFunction;
import io.kalenz.bms.access.control.entity.Privilege;
import io.kalenz.bms.access.control.entity.Resource;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ReferenceDataMapper {

    ActionDto toActionDto(Action action);

    List<ActionDto> toActionDtoList(List<Action> actions);

    ResourceDto toResourceDto(Resource resource);

    List<ResourceDto> toResourceDtoList(List<Resource> resources);

    BusinessFunctionDto toBusinessFunctionDto(BusinessFunction businessFunction);

    List<BusinessFunctionDto> toBusinessFunctionDtoList(List<BusinessFunction> functions);

    PrivilegeDto toPrivilegeDto(Privilege privilege);

    List<PrivilegeDto> toPrivilegeDtoList(List<Privilege> privileges);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    void updateResourceFromRequest(@MappingTarget Resource target, ResourceRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    void updateBusinessFunctionFromRequest(@MappingTarget BusinessFunction target, BusinessFunctionRequest request);
}
