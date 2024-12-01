package org.mangorage.cmd.api;

import java.util.Map;

public interface ICommand<S> {
    int execute(ICommandSourceStack<S> commandSourceStack);
    Map<String, IArgumentType<?>> getParameters();
}
