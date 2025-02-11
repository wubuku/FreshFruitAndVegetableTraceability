package org.dddml.ffvtraceability.domain.common;

import org.dddml.ffvtraceability.domain.Command;
import org.dddml.ffvtraceability.domain.listener.OrderFulfillmentListener;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;

public class TempCommand implements Command {
    private final String commandType;

    public TempCommand(String commandType) {
        this.commandType = commandType;
    }

    @Override
    public String getCommandType() {
        return commandType;
    }

    @Override
    public void setCommandType(String commandType) {
    }

    @Override
    public String getCommandId() {
        return UUID.randomUUID().toString();
    }

    @Override
    public void setCommandId(String commandId) {
    }

    @Override
    public String getRequesterId() {
        return OrderFulfillmentListener.class.getName();
    }

    @Override
    public void setRequesterId(String requesterId) {
    }

    @Override
    public Map<String, Object> getCommandContext() {
        return Collections.emptyMap();
    }

}
