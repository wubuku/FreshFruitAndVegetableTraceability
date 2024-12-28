package org.dddml.ffvtraceability.specialization;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Represents the context for executing property-related commands, encapsulating
 * the command content and all necessary information for managing property
 * state.
 */
public interface PropertyCommandContext<TContent, TState> {
    /**
     * Gets the content of the command.
     */
    TContent getContent();

    /**
     * Gets the state supplier (getter) for the property.
     */
    Supplier<TState> getStateGetter();

    /**
     * Gets the state consumer (setter) for the property.
     */
    Consumer<TState> getStateSetter();

    /**
     * Gets the type identifier of the outer command.
     */
    String getOuterCommandType();

    /**
     * Gets the execution environment context.
     */
    Object getExecutionEnvironment();
}
