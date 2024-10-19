package org.dddml.ffvtraceability.domain.attributesetinstance;

import java.util.UUID;

import org.dddml.ffvtraceability.domain.attributesetinstance.AttributeSetInstanceCommand.CreateAttributeSetInstance;
import org.dddml.ffvtraceability.specialization.IdGenerator;

public class AttributeSetInstanceIdGenerator implements
        IdGenerator<String, AttributeSetInstanceCommand.CreateAttributeSetInstance, AttributeSetInstanceState> {
    @Override
    public boolean isArbitraryIdEnabled() {
        return true;
    }

    @Override
    public String generateId(CreateAttributeSetInstance command) {
        return command.getProperties().hashCode() + ""; // TODO: use a better hash function
    }

    @Override
    public String getNextId() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    @Override
    public boolean equals(CreateAttributeSetInstance command, AttributeSetInstanceState state) {
        return command.getProperties().equals(state.getProperties());
    }
}
