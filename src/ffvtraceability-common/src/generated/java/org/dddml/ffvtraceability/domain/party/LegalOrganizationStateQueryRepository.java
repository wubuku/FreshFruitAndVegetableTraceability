// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain.party;

import java.util.Map;
import java.util.List;
import org.dddml.support.criterion.Criterion;
import java.time.OffsetDateTime;
import org.dddml.ffvtraceability.domain.*;

public interface LegalOrganizationStateQueryRepository {
    LegalOrganizationState get(String id);

    Iterable<LegalOrganizationState> getAll(Integer firstResult, Integer maxResults);
    
    Iterable<LegalOrganizationState> get(Iterable<Map.Entry<String, Object>> filter, List<String> orders, Integer firstResult, Integer maxResults);

    Iterable<LegalOrganizationState> get(Criterion filter, List<String> orders, Integer firstResult, Integer maxResults);

    LegalOrganizationState getFirst(Iterable<Map.Entry<String, Object>> filter, List<String> orders);

    LegalOrganizationState getFirst(Map.Entry<String, Object> keyValue, List<String> orders);

    Iterable<LegalOrganizationState> getByProperty(String propertyName, Object propertyValue, List<String> orders, Integer firstResult, Integer maxResults);

    long getCount(Iterable<Map.Entry<String, Object>> filter);

    long getCount(Criterion filter);

    PartyIdentificationState getPartyIdentification(String partyId, String partyIdentificationTypeId);

    Iterable<PartyIdentificationState> getPartyIdentifications(String partyId, Criterion filter, List<String> orders);

}
