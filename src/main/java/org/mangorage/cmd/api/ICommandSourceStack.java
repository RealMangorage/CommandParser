package org.mangorage.cmd.api;

import java.util.Map;

public interface ICommandSourceStack<S> {
    String[] getArgs();
    String[] getRemainingArgs();

    void shrinkArgs();

    void updateParameters(Map<String, IArgumentType<?>> parameters);
    <O> O getParameter(String id, IArgumentType<O> parser);

    S getContext();
}
