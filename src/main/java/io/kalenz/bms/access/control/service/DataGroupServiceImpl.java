package io.kalenz.bms.access.control.service;

import io.kalenz.approval.generated.api.access.control.client.v1.model.DataGroupDto;
import io.kalenz.approval.generated.api.access.control.client.v1.model.DataGroupItemDto;
import io.kalenz.approval.generated.api.access.control.client.v1.model.DataGroupItemsRequest;
import io.kalenz.approval.generated.api.access.control.client.v1.model.DataGroupRequest;
import io.kalenz.bms.access.control.entity.DataGroup;
import io.kalenz.bms.access.control.entity.DataGroupItem;
import io.kalenz.bms.access.control.entity.ServiceAgreement;
import io.kalenz.bms.access.control.mapper.DataGroupMapper;
import io.kalenz.bms.access.control.repository.DataGroupItemRepository;
import io.kalenz.bms.access.control.repository.DataGroupRepository;
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
public class DataGroupServiceImpl implements DataGroupService {

    private final DataGroupRepository dataGroupRepository;
    private final DataGroupItemRepository dataGroupItemRepository;
    private final ServiceAgreementRepository serviceAgreementRepository;
    private final DataGroupMapper dataGroupMapper;

    @Override
    @Transactional(readOnly = true)
    public List<DataGroupDto> listDataGroupsByServiceAgreement(String saId, String type) {
        if (!serviceAgreementRepository.existsById(saId)) {
            throw NotFoundException.of("ServiceAgreement", saId);
        }
        if (type == null) {
            return dataGroupRepository.findByServiceAgreementId(saId).stream()
                    .map(dataGroupMapper::toDto)
                    .toList();
        } else {
            return dataGroupRepository.findByServiceAgreementId(saId).stream()
                    .filter(dg -> type.equals(dg.getType()))
                    .map(dataGroupMapper::toDto)
                    .toList();
        }
    }

    @Override
    public DataGroupDto createDataGroup(String saId, DataGroupRequest request) {
        ServiceAgreement serviceAgreement = serviceAgreementRepository.findById(saId)
                .orElseThrow(() -> NotFoundException.of("ServiceAgreement", saId));

        dataGroupRepository.findByName(request.getName()).ifPresent(existing -> {
            throw new ConflictException("DataGroup already exists with name: " + request.getName());
        });

        DataGroup dataGroup = new DataGroup();
        dataGroup.setName(request.getName());
        dataGroup.setType(request.getType());
        dataGroup.setDescription(request.getDescription());
        dataGroup.setServiceAgreement(serviceAgreement);
        dataGroup.setCreatedAt(Instant.now());
        dataGroup.setCreatedBy("system");
        dataGroup.setUpdatedAt(Instant.now());
        dataGroup.setUpdatedBy("system");

        return dataGroupMapper.toDto(dataGroupRepository.save(dataGroup));
    }

    @Override
    @Transactional(readOnly = true)
    public DataGroupDto getDataGroup(String id) {
        return dataGroupMapper.toDto(dataGroupRepository.findById(id)
                .orElseThrow(() -> NotFoundException.of("DataGroup", id)));
    }

    @Override
    public DataGroupDto updateDataGroup(String id, DataGroupRequest request) {
        DataGroup dataGroup = dataGroupRepository.findById(id)
                .orElseThrow(() -> NotFoundException.of("DataGroup", id));

        dataGroupMapper.updateDataGroupFromRequest(dataGroup, request);
        dataGroup.setUpdatedAt(Instant.now());
        dataGroup.setUpdatedBy("system");

        return dataGroupMapper.toDto(dataGroupRepository.save(dataGroup));
    }

    @Override
    public void deleteDataGroup(String id) {
        DataGroup dataGroup = dataGroupRepository.findById(id)
                .orElseThrow(() -> NotFoundException.of("DataGroup", id));
        dataGroupRepository.delete(dataGroup);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DataGroupItemDto> listDataGroupItems(String id) {
        if (!dataGroupRepository.existsById(id)) {
            throw NotFoundException.of("DataGroup", id);
        }
        return dataGroupItemRepository.findByDataGroupId(id).stream()
                .map(dataGroupMapper::toDto)
                .toList();
    }

    @Override
    public List<DataGroupItemDto> setDataGroupItems(String id, DataGroupItemsRequest request) {
        DataGroup dataGroup = dataGroupRepository.findById(id)
                .orElseThrow(() -> NotFoundException.of("DataGroup", id));

        dataGroupItemRepository.deleteAll(dataGroupItemRepository.findByDataGroupId(id));

        List<DataGroupItem> newItems = request.getDataItemIds().stream().map(dataItemId -> {
            DataGroupItem item = new DataGroupItem();
            item.setDataGroup(dataGroup);
            item.setDataItemId(dataItemId);
            return item;
        }).toList();

        dataGroupItemRepository.saveAll(newItems);

        return dataGroupItemRepository.findByDataGroupId(id).stream()
                .map(dataGroupMapper::toDto)
                .toList();
    }

    @Override
    public void addDataGroupItems(String id, DataGroupItemsRequest request) {
        DataGroup dataGroup = dataGroupRepository.findById(id)
                .orElseThrow(() -> NotFoundException.of("DataGroup", id));

        List<DataGroupItem> newItems = request.getDataItemIds().stream().map(dataItemId -> {
            DataGroupItem item = new DataGroupItem();
            item.setDataGroup(dataGroup);
            item.setDataItemId(dataItemId);
            return item;
        }).toList();

        dataGroupItemRepository.saveAll(newItems);
    }

    @Override
    public void removeDataGroupItem(String id, String itemId) {
        DataGroupItem item = dataGroupItemRepository.findById(itemId)
                .orElseThrow(() -> NotFoundException.of("DataGroupItem", itemId));

        if (!item.getDataGroup().getId().equals(id)) {
            throw NotFoundException.of("DataGroupItem", itemId);
        }

        dataGroupItemRepository.delete(item);
    }
}
