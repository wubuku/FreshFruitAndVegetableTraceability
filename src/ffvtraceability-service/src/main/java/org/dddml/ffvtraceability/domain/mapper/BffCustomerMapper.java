package org.dddml.ffvtraceability.domain.mapper;


import org.dddml.ffvtraceability.domain.BffCustomerDto;
import org.dddml.ffvtraceability.domain.party.AbstractPartyCommand;
import org.dddml.ffvtraceability.domain.party.PartyState;
import org.dddml.ffvtraceability.domain.repository.BffCustomerProjection;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BffCustomerMapper {

    // 将 partyState 中的 statusId 属性映射为 BffCustomerDto 中的 active 属性。
    // 注意，原来 partyState 中的 statusId 映射为 BffCustomerDto 中的 statusId 属性的逻辑仍然保留。
    // 也就是说，partyState 中的 statusId 属性映射为 BffCustomerDto 中的两个属性：statusId 和 active
    // statusId 目前的取值为 "ACTIVE" 或 "INACTIVE"，对应的 active 的取值为 "Y" 或 "N"
    String PARTY_STATUS_ACTIVE = "ACTIVE";
    //String PARTY_STATUS_INACTIVE = "INACTIVE";

    @Mapping(target = "active", expression = "java(PARTY_STATUS_ACTIVE.equals(partyState.getStatusId()) " +
            "|| partyState.getStatusId() == null ? \"Y\" : \"N\")")
    BffCustomerDto toBffCustomerDto(PartyState partyState);

    @Mapping(target = "active", expression = "java(PARTY_STATUS_ACTIVE.equals(bffCustomerProjection.getStatusId())" +
            " || bffCustomerProjection.getStatusId() == null? \"Y\" : \"N\")")
    BffCustomerDto toBffCustomerDto(BffCustomerProjection bffCustomerProjection);

    AbstractPartyCommand.SimpleCreateParty toCreateParty(BffCustomerDto bffCustomerDto);

    AbstractPartyCommand.SimpleMergePatchParty toMergePatchParty(BffCustomerDto bffCustomerDto);
}
