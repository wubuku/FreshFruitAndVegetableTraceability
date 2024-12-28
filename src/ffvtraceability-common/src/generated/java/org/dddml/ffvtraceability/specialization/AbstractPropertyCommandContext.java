package org.dddml.ffvtraceability.specialization;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Created by Yang on 2016/7/25.
 */
public abstract class AbstractPropertyCommandContext<TContent, TState> implements PropertyCommandContext<TContent, TState> {
    private TContent content;
    private Supplier<TState> stateGetter;
    private Consumer<TState> stateSetter;
    private String outerCommandType;
    private Object executionEnvironment;

    @Override
    public TContent getContent() {
        return this.content;
    }

    public void setContent(TContent content) {
        this.content = content;
    }

    @Override
    public Supplier<TState> getStateGetter() {
        return this.stateGetter;
    }

    public void setStateGetter(Supplier<TState> stateGetter) {
        this.stateGetter = stateGetter;
    }

    @Override
    public Consumer<TState> getStateSetter() {
        return this.stateSetter;
    }

    public void setStateSetter(Consumer<TState> stateSetter) {
        this.stateSetter = stateSetter;
    }

    @Override
    public String getOuterCommandType() {
        return this.outerCommandType;
    }

    public void setOuterCommandType(String type) {
        this.outerCommandType = type;
    }

    @Override
    public Object getExecutionEnvironment() {
        return executionEnvironment;
    }

    public void setExecutionEnvironment(Object e) {
        this.executionEnvironment = e;
    }

    public static class SimplePropertyCommandContext<TContent, TState> extends AbstractPropertyCommandContext<TContent, TState> {
    }

}
