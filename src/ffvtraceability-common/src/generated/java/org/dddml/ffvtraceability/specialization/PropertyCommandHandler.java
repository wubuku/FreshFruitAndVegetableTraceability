package org.dddml.ffvtraceability.specialization;

/**
 * Defines a handler interface for processing property-specific commands.
 * 
 * @param <TContent> The type of command content this handler processes
 * @param <TState> The type of property state this handler manages
 */
public interface PropertyCommandHandler<TContent, TState> {

    /**
     * Executes the given property command.
     * 
     * @param c The property command context to execute
     */
    void execute(PropertyCommandContext<TContent, TState> c);

}
