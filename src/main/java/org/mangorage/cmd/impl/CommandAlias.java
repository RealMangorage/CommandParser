package org.mangorage.cmd.impl;

import org.mangorage.cmd.api.IArgument;
import org.mangorage.cmd.api.IArgumentType;
import org.mangorage.cmd.api.ICommand;
import org.mangorage.cmd.api.ICommandSourceStack;

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
    public String getId() {
        return id;
    }
}
