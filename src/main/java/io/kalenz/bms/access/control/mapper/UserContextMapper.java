package io.kalenz.bms.access.control.mapper;

import io.kalenz.approval.generated.api.access.control.client.v1.model.UserContextDto;
import io.kalenz.approval.generated.api.access.control.client.v1.model.UserContextState;
import io.kalenz.approval.generated.api.access.control.client.v1.model.UserContextType;
import io.kalenz.bms.access.control.entity.UserContext;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {InstantOffsetDateTimeMapper.class})
public interface UserContextMapper {

    @Mapping(source = "serviceAgreement.id", target = "serviceAgreementId")
    @Mapping(target = "state", source = "state")
    @Mapping(target = "type", source = "type")
    UserContextDto toDto(UserContext userContext);

    /** Entity → model enum. MapStruct invokes this automatically for the {@code state} field. */
    default UserContextState map(io.kalenz.bms.access.control.enumeration.UserContextState entity) {
        return entity == null ? null : UserContextState.valueOf(entity.name());
    }

    /** Entity → model enum. MapStruct invokes this automatically for the {@code type} field. */
    default UserContextType map(io.kalenz.bms.access.control.enumeration.UserContextType entity) {
        return entity == null ? null : UserContextType.valueOf(entity.name());
    }
}
