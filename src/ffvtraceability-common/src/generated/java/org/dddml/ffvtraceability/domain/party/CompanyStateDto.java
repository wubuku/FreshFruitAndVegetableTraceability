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


public class CompanyStateDto extends LegalOrganizationStateDto {


    public static class DtoConverter extends AbstractStateDtoConverter
    {
        public static Collection<String> collectionFieldNames = Arrays.asList(new String[]{"PartyIdentifications"});

        @Override
        protected boolean isCollectionField(String fieldName) {
            return CollectionUtils.collectionContainsIgnoringCase(collectionFieldNames, fieldName);
        }

        public CompanyStateDto[] toCompanyStateDtoArray(Iterable<PartyState> states) {
            return toCompanyStateDtoList(states).toArray(new CompanyStateDto[0]);
        }

        public List<CompanyStateDto> toCompanyStateDtoList(Iterable<PartyState> states) {
            ArrayList<CompanyStateDto> stateDtos = new ArrayList();
            for (PartyState s : states) {
                CompanyStateDto dto = toCompanyStateDto(s);
                stateDtos.add(dto);
            }
            return stateDtos;
        }

        public CompanyStateDto toCompanyStateDto(PartyState state)
        {
            if(state == null) {
                return null;
            }
            CompanyStateDto dto = new CompanyStateDto();
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
            if (returnedFieldsContains("ShortDescription")) {
                dto.setShortDescription(state.getShortDescription());
            }
            if (returnedFieldsContains("Email")) {
                dto.setEmail(state.getEmail());
            }
            if (returnedFieldsContains("WebSite")) {
                dto.setWebSite(state.getWebSite());
            }
            if (returnedFieldsContains("Telephone")) {
                dto.setTelephone(state.getTelephone());
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

          // ////////////////
          if (state instanceof LegalOrganizationState) {
            LegalOrganizationState ss = (LegalOrganizationState) state;
            dto.setPartyTypeId(PartyTypeId.LEGAL_ORGANIZATION);
          }
          // ////////////////

          // ////////////////
          if (state instanceof CompanyState) {
            CompanyState ss = (CompanyState) state;
            dto.setPartyTypeId(PartyTypeId.COMPANY);
          }
          // ////////////////
            return dto;
        }

    }
}

