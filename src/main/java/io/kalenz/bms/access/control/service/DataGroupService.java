package io.kalenz.bms.access.control.service;

import io.kalenz.approval.generated.api.access.control.client.v1.model.DataGroupDto;
import io.kalenz.approval.generated.api.access.control.client.v1.model.DataGroupItemDto;
import io.kalenz.approval.generated.api.access.control.client.v1.model.DataGroupItemsRequest;
import io.kalenz.approval.generated.api.access.control.client.v1.model.DataGroupRequest;

import java.util.List;

public interface DataGroupService {

    List<DataGroupDto> listDataGroupsByServiceAgreement(String saId, String type);

    DataGroupDto createDataGroup(String saId, DataGroupRequest request);

    DataGroupDto getDataGroup(String id);

    DataGroupDto updateDataGroup(String id, DataGroupRequest request);

    void deleteDataGroup(String id);

    List<DataGroupItemDto> listDataGroupItems(String id);

    List<DataGroupItemDto> setDataGroupItems(String id, DataGroupItemsRequest request);

    void addDataGroupItems(String id, DataGroupItemsRequest request);

    void removeDataGroupItem(String id, String itemId);
}
