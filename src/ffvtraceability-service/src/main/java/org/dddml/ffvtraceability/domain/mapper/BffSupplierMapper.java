package org.dddml.ffvtraceability.domain.mapper;


import org.dddml.ffvtraceability.domain.BffSupplierDto;
import org.dddml.ffvtraceability.domain.party.AbstractPartyCommand;
import org.dddml.ffvtraceability.domain.party.PartyState;
import org.dddml.ffvtraceability.domain.repository.BffSupplierProjection;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BffSupplierMapper {

    // 将 partyState 中的 statusId 属性映射为 BffSupplierDto 中的 active 属性。
    // 注意，原来 partyState 中的 statusId 映射为 BffSupplierDto 中的 statusId 属性的逻辑仍然保留。
    // 也就是说，partyState 中的 statusId 属性映射为 BffSupplierDto 中的两个属性：statusId 和 active
    // statusId 目前的取值为 "ACTIVE" 或 "INACTIVE"，对应的 active 的取值为 "Y" 或 "N"
    String PARTY_STATUS_ACTIVE = "ACTIVE";
    //String PARTY_STATUS_INACTIVE = "INACTIVE";

    @Mapping(target = "active", expression = "java(PARTY_STATUS_ACTIVE.equals(partyState.getStatusId()) " +
            "|| partyState.getStatusId() == null ? \"Y\" : \"N\")")
    BffSupplierDto toBffSupplierDto(PartyState partyState);

    @Mapping(target = "active", expression = "java(PARTY_STATUS_ACTIVE.equals(bffSupplierProjection.getStatusId())" +
            " || bffSupplierProjection.getStatusId() == null? \"Y\" : \"N\")")
    BffSupplierDto toBffSupplierDto(BffSupplierProjection bffSupplierProjection);

    AbstractPartyCommand.SimpleCreateParty toCreateParty(BffSupplierDto bffSupplierDto);

    AbstractPartyCommand.SimpleMergePatchParty toMergePatchParty(BffSupplierDto bffSupplierDto);
}
