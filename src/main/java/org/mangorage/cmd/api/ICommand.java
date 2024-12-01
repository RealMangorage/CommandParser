package org.mangorage.cmd.api;

import java.util.Map;

public interface ICommand<S> {
    void execute(ICommandSourceStack<S> commandSourceStack);
    Map<String, IArgumentType<?>> getParameters();
}
