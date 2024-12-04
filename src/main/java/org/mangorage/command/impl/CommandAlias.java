package org.mangorage.command.impl;

import org.mangorage.command.api.IArgument;
import org.mangorage.command.api.ICommand;
import org.mangorage.command.api.ICommandSourceStack;

import java.util.Map;

public final class CommandAlias<S> implements ICommand<S> {

    public static <S> ICommand<S> of(String id, ICommand<S> actual) {
        return new CommandAlias<>(id, actual);
    }

    private final String id;
    private final ICommand<S> actual;

    private CommandAlias(String id, ICommand<S> actual) {
        this.id = id;
        this.actual = actual;
    }

    @Override
    public int execute(ICommandSourceStack<S> commandSourceStack) {
        return actual.execute(commandSourceStack);
    }

    @Override
    public Map<String, IArgument<S>> getArguments() {
        return actual.getArguments();
    }

    @Override
    public ICommand<S> createCommandAlias(String id) {
        return of(id, this);
    }

    @Override
    public String getId() {
        return id;
    }
}
