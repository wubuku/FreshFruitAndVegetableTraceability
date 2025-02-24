package org.dddml.ffvtraceability.specialization;

import java.util.function.Function;

public interface MutationContext<T, TM> {

    /**
     * Creates a new MutationContext instance with the specified event and state converter.
     *
     * @param e                       The event that triggers the mutation
     * @param toMutableStateConverter A function that converts an immutable state to a mutable state
     * @param <T>                     The type of the immutable state
     * @param <TM>                    The type of the mutable state
     * @return A new MutationContext instance
     */
    static <T, TM> MutationContext<T, TM> of(Event e, Function<T, TM> toMutableStateConverter) {
        return new MutationContext<T, TM>() {
            @Override
            public Event getEvent() {
                return e;
            }

            @Override
            public TM toMutableState(T state) {
                return toMutableStateConverter.apply(state);
            }

            @Override
            public TM newMutableStateById(Object id) {
                throw new UnsupportedOperationException("This context does not support creating new mutable state by ID");
            }
        };
    }

    /**
     * Creates a new MutationContext instance that supports creating new mutable state by ID.
     *
     * @param e                    The event that triggers the mutation
     * @param mutableStateFactory  A function that creates new mutable state from an ID
     * @param <T>                  The type of the immutable state
     * @param <TM>                 The type of the mutable state
     * @return A new MutationContext instance
     */
    static <T, TM> MutationContext<T, TM> forCreation(Event e, Function<Object, TM> mutableStateFactory) {
        return new MutationContext<T, TM>() {
            @Override
            public Event getEvent() {
                return e;
            }

            @Override
            public TM toMutableState(T state) {
                throw new UnsupportedOperationException("This context only supports creating new mutable state by ID");
            }

            @Override
            public TM newMutableStateById(Object id) {
                return mutableStateFactory.apply(id);
            }
        };
    }
    //NOTE: 已将 createMutableState 重命名为 toMutableState。因为 createMutableState 听起来像是创建一个新对象。
    //    它的一般实现，其实是如果一个状态对象（T state）本来是可变的，那么就直接返回它；
    //    如果是不可变的，那么才会创建并返回一个可变的状态对象。
    //    把这个方法改名为 toMutableState() 似乎更合理。

    Event getEvent();

    /**
     * Converts a state object to its mutable form.
     * If the state is already mutable, returns it directly.
     * If the state is immutable, creates and returns a new mutable copy.
     *
     * @param state The state to convert
     * @return The mutable form of the state
     */
    TM toMutableState(T state);

    /**
     * Creates a new mutable state instance using the provided ID.
     *
     * @param id The ID to use for creating the new state
     * @return A new mutable state instance
     */
    TM newMutableStateById(Object id);
}
