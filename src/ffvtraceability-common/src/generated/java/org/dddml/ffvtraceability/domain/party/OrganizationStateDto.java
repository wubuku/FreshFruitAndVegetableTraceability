// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain.party;

import java.util.*;
import java.math.*;
import java.time.OffsetDateTime;
import org.dddml.ffvtraceability.domain.*;
import org.dddml.ffvtraceability.specialization.*;


public class OrganizationStateDto extends PartyStateDto {


    public static class DtoConverter extends AbstractStateDtoConverter
    {
        public static Collection<String> collectionFieldNames = Arrays.asList(new String[]{"PartyIdentifications"});

        @Override
        protected boolean isCollectionField(String fieldName) {
            return CollectionUtils.collectionContainsIgnoringCase(collectionFieldNames, fieldName);
        }

        public OrganizationStateDto[] toOrganizationStateDtoArray(Iterable<PartyState> states) {
            return toOrganizationStateDtoList(states).toArray(new OrganizationStateDto[0]);
        }

        public List<OrganizationStateDto> toOrganizationStateDtoList(Iterable<PartyState> states) {
            ArrayList<OrganizationStateDto> stateDtos = new ArrayList();
            for (PartyState s : states) {
                OrganizationStateDto dto = toOrganizationStateDto(s);
                stateDtos.add(dto);
            }
            return stateDtos;
        }

        public OrganizationStateDto toOrganizationStateDto(PartyState state)
        {
            if(state == null) {
                return null;
            }
            OrganizationStateDto dto = new OrganizationStateDto();
            if (returnedFieldsContains("PartyId")) {
                dto.setPartyId(state.getPartyId());
            }
            if (returnedFieldsContains("PartyTypeId")) {
                dto.setPartyTypeId(state.getPartyTypeId());
            }
            if (returnedFieldsContains("PrimaryRoleTypeId")) {
                dto.setPrimaryRoleTypeId(state.getPrimaryRoleTypeId());
            }
            if (returnedFieldsContains("ExternalId")) {
                dto.setExternalId(state.getExternalId());
            }
            if (returnedFieldsContains("PreferredCurrencyUomId")) {
                dto.setPreferredCurrencyUomId(state.getPreferredCurrencyUomId());
            }
            if (returnedFieldsContains("Description")) {
                dto.setDescription(state.getDescription());
            }
            if (returnedFieldsContains("StatusId")) {
                dto.setStatusId(state.getStatusId());
            }
            if (returnedFieldsContains("Version")) {
                dto.setVersion(state.getVersion());
            }
            if (returnedFieldsContains("CreatedBy")) {
                dto.setCreatedBy(state.getCreatedBy());
            }
            if (returnedFieldsContains("CreatedAt")) {
                dto.setCreatedAt(state.getCreatedAt());
            }
            if (returnedFieldsContains("UpdatedBy")) {
                dto.setUpdatedBy(state.getUpdatedBy());
            }
            if (returnedFieldsContains("UpdatedAt")) {
                dto.setUpdatedAt(state.getUpdatedAt());
            }
            if (returnedFieldsContains("PartyIdentifications")) {
                ArrayList<PartyIdentificationStateDto> arrayList = new ArrayList();
                if (state.getPartyIdentifications() != null) {
                    PartyIdentificationStateDto.DtoConverter conv = new PartyIdentificationStateDto.DtoConverter();
                    String returnFS = CollectionUtils.mapGetValueIgnoringCase(getReturnedFields(), "PartyIdentifications");
                    if(returnFS != null) { conv.setReturnedFieldsString(returnFS); } else { conv.setAllFieldsReturned(this.getAllFieldsReturned()); }
                    for (PartyIdentificationState s : state.getPartyIdentifications()) {
                        arrayList.add(conv.toPartyIdentificationStateDto(s));
                    }
                }
                dto.setPartyIdentifications(arrayList.toArray(new PartyIdentificationStateDto[0]));
            }

          // ////////////////
          if (state instanceof OrganizationState) {
            OrganizationState ss = (OrganizationState) state;
            dto.setPartyTypeId(PartyTypeId.ORGANIZATION);
            if (returnedFieldsContains("OrganizationName")) {
                dto.setOrganizationName(ss.getOrganizationName());
            }
          }
          // ////////////////
            return dto;
        }

    }
}
