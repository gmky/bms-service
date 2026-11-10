package io.kalenz.bms.access.control.service;

import io.kalenz.approval.generated.api.access.control.client.v1.model.ServiceAgreementDto;
import io.kalenz.approval.generated.api.access.control.client.v1.model.ServiceAgreementRequest;
import io.kalenz.approval.generated.api.access.control.client.v1.model.ServiceAgreementState;
import io.kalenz.approval.generated.api.access.control.client.v1.model.ServiceAgreementStateRequest;

import java.util.List;

public interface ServiceAgreementService {

    List<ServiceAgreementDto> listServiceAgreements(ServiceAgreementState state);

    ServiceAgreementDto createServiceAgreement(ServiceAgreementRequest request);

    ServiceAgreementDto getServiceAgreement(String id);

    ServiceAgreementDto updateServiceAgreement(String id, ServiceAgreementRequest request);

    void deleteServiceAgreement(String id);

    ServiceAgreementDto updateServiceAgreementState(String id, ServiceAgreementStateRequest request);
}
