// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain.partycontactmech;

import java.util.Map;
import java.util.List;
import org.dddml.support.criterion.Criterion;
import java.time.OffsetDateTime;
import org.dddml.ffvtraceability.domain.*;
import org.dddml.ffvtraceability.specialization.Event;
import org.dddml.ffvtraceability.domain.Command;

public interface PartyContactMechApplicationService {
    void when(PartyContactMechCommand.CreatePartyContactMech c);

    void when(PartyContactMechCommand.MergePatchPartyContactMech c);

    PartyContactMechState get(PartyContactMechId id);

    Iterable<PartyContactMechState> getAll(Integer firstResult, Integer maxResults);

    Iterable<PartyContactMechState> get(Iterable<Map.Entry<String, Object>> filter, List<String> orders, Integer firstResult, Integer maxResults);

    Iterable<PartyContactMechState> get(Criterion filter, List<String> orders, Integer firstResult, Integer maxResults);

    Iterable<PartyContactMechState> getByProperty(String propertyName, Object propertyValue, List<String> orders, Integer firstResult, Integer maxResults);

    long getCount(Iterable<Map.Entry<String, Object>> filter);

    long getCount(Criterion filter);

    PartyContactMechEvent getEvent(PartyContactMechId partyContactMechId, long version);

    PartyContactMechState getHistoryState(PartyContactMechId partyContactMechId, long version);

    PartyContactMechPurposeState getPartyContactMechPurpose(PartyContactMechId partyContactMechId, String contactMechPurposeTypeId);

    Iterable<PartyContactMechPurposeState> getPartyContactMechPurposes(PartyContactMechId partyContactMechId, Criterion filter, List<String> orders);

}

