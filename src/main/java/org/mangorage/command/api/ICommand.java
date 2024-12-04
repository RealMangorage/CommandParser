package org.mangorage.command.api;

import java.util.Map;

public interface ICommand<S> extends IExecutable<ICommandSourceStack<S>>, IdHolder {
    Map<String, IArgument<S>> getArguments();
    ICommand<S> createCommandAlias(String id);
}
