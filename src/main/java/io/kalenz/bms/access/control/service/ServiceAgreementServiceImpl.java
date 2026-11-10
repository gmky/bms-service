package io.kalenz.bms.access.control.service;

import io.kalenz.approval.generated.api.access.control.client.v1.model.ServiceAgreementDto;
import io.kalenz.approval.generated.api.access.control.client.v1.model.ServiceAgreementRequest;
import io.kalenz.approval.generated.api.access.control.client.v1.model.ServiceAgreementState;
import io.kalenz.approval.generated.api.access.control.client.v1.model.ServiceAgreementStateRequest;
import io.kalenz.bms.access.control.entity.ServiceAgreement;
import io.kalenz.bms.access.control.mapper.EnumConverter;
import io.kalenz.bms.access.control.mapper.ServiceAgreementMapper;
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
public class ServiceAgreementServiceImpl implements ServiceAgreementService {

    private final ServiceAgreementRepository serviceAgreementRepository;
    private final ServiceAgreementMapper serviceAgreementMapper;
    private final EnumConverter enumConverter;

    @Override
    @Transactional(readOnly = true)
    public List<ServiceAgreementDto> listServiceAgreements(ServiceAgreementState state) {
        if (state == null) {
            return serviceAgreementMapper.toServiceAgreementDtoList(serviceAgreementRepository.findAll());
        }
        return serviceAgreementMapper.toServiceAgreementDtoList(
                serviceAgreementRepository.findByState(enumConverter.toEntityServiceAgreementState(state)));
    }

    @Override
    public ServiceAgreementDto createServiceAgreement(ServiceAgreementRequest request) {
        if (serviceAgreementRepository.findByExternalId(request.getExternalId()).isPresent()) {
            throw new ConflictException("ServiceAgreement already exists with externalId: " + request.getExternalId());
        }
        ServiceAgreement sa = serviceAgreementMapper.toServiceAgreement(request);
        Instant now = Instant.now();
        sa.setCreatedAt(now);
        sa.setUpdatedAt(now);
        sa.setStateChangedAt(now);
        return serviceAgreementMapper.toServiceAgreementDto(serviceAgreementRepository.save(sa));
    }

    @Override
    @Transactional(readOnly = true)
    public ServiceAgreementDto getServiceAgreement(String id) {
        ServiceAgreement sa = serviceAgreementRepository.findById(id)
                .orElseThrow(() -> NotFoundException.of("ServiceAgreement", id));
        return serviceAgreementMapper.toServiceAgreementDto(sa);
    }

    @Override
    public ServiceAgreementDto updateServiceAgreement(String id, ServiceAgreementRequest request) {
        ServiceAgreement sa = serviceAgreementRepository.findById(id)
                .orElseThrow(() -> NotFoundException.of("ServiceAgreement", id));
        serviceAgreementMapper.updateServiceAgreementFromRequest(sa, request);
        sa.setUpdatedAt(Instant.now());
        return serviceAgreementMapper.toServiceAgreementDto(serviceAgreementRepository.save(sa));
    }

    @Override
    public void deleteServiceAgreement(String id) {
        ServiceAgreement sa = serviceAgreementRepository.findById(id)
                .orElseThrow(() -> NotFoundException.of("ServiceAgreement", id));
        Instant now = Instant.now();
        sa.setState(enumConverter.toEntityServiceAgreementState(ServiceAgreementState.DELETED));
        sa.setStateChangedAt(now);
        sa.setUpdatedAt(now);
        serviceAgreementRepository.save(sa);
    }

    @Override
    public ServiceAgreementDto updateServiceAgreementState(String id, ServiceAgreementStateRequest request) {
        ServiceAgreement sa = serviceAgreementRepository.findById(id)
                .orElseThrow(() -> NotFoundException.of("ServiceAgreement", id));
        Instant now = Instant.now();
        sa.setState(enumConverter.toEntityServiceAgreementState(request.getState()));
        sa.setStateChangedAt(now);
        sa.setUpdatedAt(now);
        return serviceAgreementMapper.toServiceAgreementDto(serviceAgreementRepository.save(sa));
    }
}
