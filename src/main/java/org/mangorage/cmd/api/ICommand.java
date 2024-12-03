package org.mangorage.cmd.api;

import java.util.Map;

public interface ICommand<S> extends IExecutable<ICommandSourceStack<S>>, IdHolder {
    Map<String, IArgumentType<?>> getParameters();
}
