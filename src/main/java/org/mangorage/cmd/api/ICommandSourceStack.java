package org.mangorage.cmd.api;

import org.mangorage.cmd.impl.argument.ParseError;

import java.util.Map;

public interface ICommandSourceStack<S> {
    String[] getArgs();
    String[] getRemainingArgs();

    void shrinkArgs();

    void updateParameters(Map<String, IArgumentType<?>> parameters);
    <O> O getParameter(String id, IArgumentType<O> parser);

    Map<String, ParseError> getParsingErrors();
    S getContext();
}
