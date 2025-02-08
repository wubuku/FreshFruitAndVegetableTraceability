package org.dddml.ffvtraceability.specialization;

import java.util.List;

public interface DomainEventPublisher {

    <T> void publish(Class<T> aggregateType, Object aggregateId, List<Event> domainEvents);

    //void publish(String aggregateType, Object aggregateId, List<Event> domainEvents);
}
