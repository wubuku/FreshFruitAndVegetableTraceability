// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain.contactmech;

import java.util.*;
import java.math.*;
import java.time.OffsetDateTime;
import org.dddml.ffvtraceability.domain.*;
import org.dddml.ffvtraceability.specialization.*;


public class TelecomNumberStateDto extends ContactMechStateDto {


    public static class DtoConverter extends AbstractStateDtoConverter
    {
        public static Collection<String> collectionFieldNames = Arrays.asList(new String[]{});

        @Override
        protected boolean isCollectionField(String fieldName) {
            return CollectionUtils.collectionContainsIgnoringCase(collectionFieldNames, fieldName);
        }

        public TelecomNumberStateDto[] toTelecomNumberStateDtoArray(Iterable<ContactMechState> states) {
            return toTelecomNumberStateDtoList(states).toArray(new TelecomNumberStateDto[0]);
        }

        public List<TelecomNumberStateDto> toTelecomNumberStateDtoList(Iterable<ContactMechState> states) {
            ArrayList<TelecomNumberStateDto> stateDtos = new ArrayList();
            for (ContactMechState s : states) {
                TelecomNumberStateDto dto = toTelecomNumberStateDto(s);
                stateDtos.add(dto);
            }
            return stateDtos;
        }

        public TelecomNumberStateDto toTelecomNumberStateDto(ContactMechState state)
        {
            if(state == null) {
                return null;
            }
            TelecomNumberStateDto dto = new TelecomNumberStateDto();
            if (returnedFieldsContains("ContactMechId")) {
                dto.setContactMechId(state.getContactMechId());
            }
            if (returnedFieldsContains("ContactMechTypeId")) {
                dto.setContactMechTypeId(state.getContactMechTypeId());
            }
            if (returnedFieldsContains("InfoString")) {
                dto.setInfoString(state.getInfoString());
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

          // ////////////////
          if (state instanceof TelecomNumberState) {
            TelecomNumberState ss = (TelecomNumberState) state;
            dto.setContactMechTypeId(ContactMechTypeId.TELECOM_NUMBER);
            if (returnedFieldsContains("CountryCode")) {
                dto.setCountryCode(ss.getCountryCode());
            }
            if (returnedFieldsContains("AreaCode")) {
                dto.setAreaCode(ss.getAreaCode());
            }
            if (returnedFieldsContains("ContactNumber")) {
                dto.setContactNumber(ss.getContactNumber());
            }
            if (returnedFieldsContains("AskForName")) {
                dto.setAskForName(ss.getAskForName());
            }
          }
          // ////////////////
            return dto;
        }

    }
}
