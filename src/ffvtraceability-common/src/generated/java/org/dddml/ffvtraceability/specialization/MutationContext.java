package org.dddml.ffvtraceability.specialization;

import java.util.function.Function;

public interface MutationContext<T, TM> {

    Event getEvent();

    TM createMutableState(T state);
    //TODO 将上面的方法重命名为 toMutableState。因为 createMutableState 听起来像是创建一个新对象。
    //    它的一般实现，其实是如果一个状态对象（T state）本来是可变的，那么就直接返回它；
    //    如果是不可变的，那么才会创建并返回一个可变的状态对象。
    //    把这个方法改名为 toMutableState() 似乎更合理。

    //TODO 增加一个 newMutableStateById() 方法。但是这个命名有点长了。
    //    考虑直接命名为 newMutableState？（有更好的名字？）

    /**
     * Creates a new MutationContext instance with the specified event and mutable state factory.
     *
     * @param e The event that triggers the mutation
     * @param mutableStateFactory A function that creates a mutable state from the immutable state
     * @param <T> The type of the immutable state
     * @param <TM> The type of the mutable state
     * @return A new MutationContext instance
     */
    static <T, TM> MutationContext<T, TM> of(Event e, Function<T, TM> mutableStateFactory) {
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
