package org.mangorage.cmd.api;

import java.util.Map;
import java.util.Optional;

public interface ICommandSourceStack<S> {
    String[] getArgs();
    String[] getRemainingArgs();

    String[] getPreviousRemainingArgs();
    void shrinkArgs();

    void updateParameters(Map<String, IArgument<S>> parameters);
    <O> O getParameter(String id, IArgumentType<O> parser);

    <O> Optional<O> getOptionalParameter(String id, IArgumentType<O> parser);
    S getContext();
}
