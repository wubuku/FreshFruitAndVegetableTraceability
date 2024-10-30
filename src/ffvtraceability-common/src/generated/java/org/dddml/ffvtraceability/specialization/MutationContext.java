package org.dddml.ffvtraceability.specialization;

import java.util.function.Function;

public interface MutationContext<T, TM> {

    Event getEvent();

    TM createMutableState(T state);

    /**
     * Creates a new MutationContext instance with the specified event and mutable state factory.
     *
     * @param e The event that triggers the mutation
     * @param mutableStateFactory A function that creates a mutable state from the immutable state
     * @param <T> The type of the immutable state
     * @param <TM> The type of the mutable state
     * @return A new MutationContext instance
     */
    static <T, TM> MutationContext of(Event e, Function<T, TM> mutableStateFactory) {
        return new MutationContext<T, TM>() {
            @Override
            public Event getEvent() {
                return e;
            }

            @Override
            public TM createMutableState(T state) {
                return mutableStateFactory.apply(state);
            }
        };
    }

}
