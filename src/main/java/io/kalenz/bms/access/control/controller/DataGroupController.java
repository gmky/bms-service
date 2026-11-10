package io.kalenz.bms.access.control.controller;

import io.kalenz.approval.generated.api.access.control.client.v1.DataGroupsApi;
import io.kalenz.approval.generated.api.access.control.client.v1.model.DataGroupDto;
import io.kalenz.approval.generated.api.access.control.client.v1.model.DataGroupItemDto;
import io.kalenz.approval.generated.api.access.control.client.v1.model.DataGroupItemsRequest;
import io.kalenz.approval.generated.api.access.control.client.v1.model.DataGroupRequest;
import io.kalenz.bms.access.control.service.DataGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class DataGroupController implements DataGroupsApi {

    private final DataGroupService dataGroupService;

    @Override
    public ResponseEntity<List<DataGroupDto>> listDataGroupsByServiceAgreement(String saId, String type) {
        return ResponseEntity.ok(dataGroupService.listDataGroupsByServiceAgreement(saId, type));
    }

    @Override
    public ResponseEntity<DataGroupDto> createDataGroup(String saId, DataGroupRequest dataGroupRequest) {
        return ResponseEntity.status(201).body(dataGroupService.createDataGroup(saId, dataGroupRequest));
    }

    @Override
    public ResponseEntity<DataGroupDto> getDataGroup(String id) {
        return ResponseEntity.ok(dataGroupService.getDataGroup(id));
    }

    @Override
    public ResponseEntity<DataGroupDto> updateDataGroup(String id, DataGroupRequest dataGroupRequest) {
        return ResponseEntity.ok(dataGroupService.updateDataGroup(id, dataGroupRequest));
    }

    @Override
    public ResponseEntity<Void> deleteDataGroup(String id) {
        dataGroupService.deleteDataGroup(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<List<DataGroupItemDto>> listDataGroupItems(String id) {
        return ResponseEntity.ok(dataGroupService.listDataGroupItems(id));
    }

    @Override
    public ResponseEntity<List<DataGroupItemDto>> setDataGroupItems(String id, DataGroupItemsRequest dataGroupItemsRequest) {
        return ResponseEntity.ok(dataGroupService.setDataGroupItems(id, dataGroupItemsRequest));
    }

    @Override
    public ResponseEntity<Void> addDataGroupItems(String id, DataGroupItemsRequest dataGroupItemsRequest) {
        dataGroupService.addDataGroupItems(id, dataGroupItemsRequest);
        return ResponseEntity.status(201).build();
    }

    @Override
    public ResponseEntity<Void> removeDataGroupItem(String id, String itemId) {
        dataGroupService.removeDataGroupItem(id, itemId);
        return ResponseEntity.noContent().build();
    }
}
