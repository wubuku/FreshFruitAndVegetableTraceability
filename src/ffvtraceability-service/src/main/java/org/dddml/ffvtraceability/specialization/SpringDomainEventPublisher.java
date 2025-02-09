package org.dddml.ffvtraceability.specialization;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.ResolvableType;
import org.springframework.core.ResolvableTypeProvider;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpringDomainEventPublisher implements DomainEventPublisher {
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Override
    public <T> void publish(Class<T> aggregateType, Object aggregateId, List<Event> domainEvents) {
        applicationEventPublisher.publishEvent(new AggregateEventEnvelope<>(aggregateType, aggregateId, domainEvents));
    }

    public static class AggregateEventEnvelope<T> implements ResolvableTypeProvider {
        private final Class<T> aggregateType;
        private final Object aggregateId;
        private final List<Event> domainEvents;

        public AggregateEventEnvelope(Class<T> aggregateType, Object aggregateId, List<Event> domainEvents) {
            this.aggregateType = aggregateType;
            this.aggregateId = aggregateId;
            this.domainEvents = domainEvents;
        }

        @Override
        public ResolvableType getResolvableType() {
            return ResolvableType.forClassWithGenerics(
                    getClass(),
                    ResolvableType.forClass(aggregateType)
            );
        }

        public Class<T> getAggregateType() {
            return aggregateType;
        }

        public Object getAggregateId() {
            return aggregateId;
        }

        public List<Event> getDomainEvents() {
            return domainEvents;
        }

        @Override
        public String toString() {
            return "AggregateEventEnvelope{" +
                    "aggregateType=" + aggregateType +
                    ", aggregateId=" + aggregateId +
                    ", domainEvents=" + domainEvents +
                    '}';
        }
    }
}
