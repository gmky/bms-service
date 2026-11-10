package io.kalenz.bms.access.control.service;

import io.kalenz.approval.generated.api.access.control.client.v1.model.ActionDto;
import io.kalenz.approval.generated.api.access.control.client.v1.model.BusinessFunctionDto;
import io.kalenz.approval.generated.api.access.control.client.v1.model.BusinessFunctionRequest;
import io.kalenz.approval.generated.api.access.control.client.v1.model.PrivilegeDto;
import io.kalenz.approval.generated.api.access.control.client.v1.model.PrivilegeRequest;
import io.kalenz.approval.generated.api.access.control.client.v1.model.ResourceDto;
import io.kalenz.approval.generated.api.access.control.client.v1.model.ResourceRequest;

import java.util.List;

public interface ReferenceDataService {

    List<ActionDto> listActions();

    ActionDto getAction(String id);

    List<ResourceDto> listResources();

    ResourceDto createResource(ResourceRequest request);

    ResourceDto getResource(String id);

    ResourceDto updateResource(String id, ResourceRequest request);

    void deleteResource(String id);

    List<BusinessFunctionDto> listBusinessFunctions();

    BusinessFunctionDto createBusinessFunction(BusinessFunctionRequest request);

    BusinessFunctionDto getBusinessFunction(String id);

    BusinessFunctionDto updateBusinessFunction(String id, BusinessFunctionRequest request);

    void deleteBusinessFunction(String id);

    List<PrivilegeDto> listPrivileges(String functionCode, String resourceCode, String actionCode);

    PrivilegeDto createPrivilege(PrivilegeRequest request);

    PrivilegeDto getPrivilege(String id);

    void deletePrivilege(String id);
}
