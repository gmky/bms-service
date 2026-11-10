package io.kalenz.bms.access.control.service;

import io.kalenz.approval.generated.api.access.control.client.v1.model.UserContextDto;
import io.kalenz.approval.generated.api.access.control.client.v1.model.UserContextRequest;
import io.kalenz.approval.generated.api.access.control.client.v1.model.UserContextState;
import io.kalenz.approval.generated.api.access.control.client.v1.model.UserContextStateRequest;
import io.kalenz.approval.generated.api.access.control.client.v1.model.UserContextType;
import io.kalenz.bms.access.control.entity.ServiceAgreement;
import io.kalenz.bms.access.control.entity.UserContext;
import io.kalenz.bms.access.control.mapper.EnumConverter;
import io.kalenz.bms.access.control.mapper.UserContextMapper;
import io.kalenz.bms.access.control.repository.ServiceAgreementRepository;
import io.kalenz.bms.access.control.repository.UserContextRepository;
import io.kalenz.bms.exception.ConflictException;
import io.kalenz.bms.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UserContextServiceImpl implements UserContextService {

    private final UserContextRepository userContextRepository;
    private final ServiceAgreementRepository serviceAgreementRepository;
    private final UserContextMapper userContextMapper;
    private final EnumConverter enumConverter;

    @Override
    public List<UserContextDto> listUsersInServiceAgreement(String saId, UserContextState modelState) {
        serviceAgreementRepository.findById(saId)
                .orElseThrow(() -> NotFoundException.of("ServiceAgreement", saId));

        List<UserContext> contexts = userContextRepository.findByServiceAgreementId(saId);

        if (modelState != null) {
            var entityState = enumConverter.toEntityUserContextState(modelState);
            contexts = contexts.stream()
                    .filter(c -> c.getState() == entityState)
                    .collect(Collectors.toList());
        }

        return contexts.stream()
                .map(userContextMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserContextDto addUserToServiceAgreement(String saId, UserContextRequest request) {
        ServiceAgreement sa = serviceAgreementRepository.findById(saId)
                .orElseThrow(() -> NotFoundException.of("ServiceAgreement", saId));

        userContextRepository.findByUserIdAndServiceAgreementId(request.getUserId(), saId)
                .ifPresent(existing -> {
                    throw new ConflictException("User already has a context in this service agreement");
                });

        UserContext context = new UserContext();
        context.setServiceAgreement(sa);
        context.setUserId(request.getUserId());
        context.setState(request.getState() != null
                ? enumConverter.toEntityUserContextState(request.getState())
                : enumConverter.toEntityUserContextState(UserContextState.ENABLED));
        context.setType(request.getType() != null
                ? enumConverter.toEntityUserContextType(request.getType())
                : enumConverter.toEntityUserContextType(UserContextType.DEFAULT));

        if (request.getStartDate() != null) {
            context.setStartDate(request.getStartDate().toInstant());
        }
        if (request.getEndDate() != null) {
            context.setEndDate(request.getEndDate().toInstant());
        }

        Instant now = Instant.now();
        context.setCreatedAt(now);
        context.setCreatedBy("system");
        context.setUpdatedAt(now);
        context.setUpdatedBy("system");
        context.setStateChangedAt(now);

        return userContextMapper.toDto(userContextRepository.save(context));
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserContextDto> listUserContexts(String userId) {
        return userContextRepository.findByUserId(userId).stream()
                .map(userContextMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public UserContextDto getUserContext(String id) {
        return userContextMapper.toDto(userContextRepository.findById(id)
                .orElseThrow(() -> NotFoundException.of("UserContext", id)));
    }

    @Override
    public void removeUserFromServiceAgreement(String id) {
        UserContext context = userContextRepository.findById(id)
                .orElseThrow(() -> NotFoundException.of("UserContext", id));
        userContextRepository.delete(context);
    }

    @Override
    public UserContextDto updateUserContextState(String id, UserContextStateRequest request) {
        UserContext context = userContextRepository.findById(id)
                .orElseThrow(() -> NotFoundException.of("UserContext", id));

        context.setState(enumConverter.toEntityUserContextState(request.getState()));

        Instant now = Instant.now();
        context.setStateChangedAt(now);
        context.setUpdatedAt(now);
        context.setUpdatedBy("system");

        return userContextMapper.toDto(userContextRepository.save(context));
    }
}
