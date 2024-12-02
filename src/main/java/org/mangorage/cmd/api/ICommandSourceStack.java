package org.mangorage.cmd.api;

import org.mangorage.cmd.impl.argument.ParseError;

import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;

public interface ICommandSourceStack<S> {
    String[] getArgs();
    String[] getRemainingArgs();

    String[] getPreviousRemainingArgs();
    void shrinkArgs();

    void updateParameters(Map<String, IArgumentType<?>> parameters);
    <O> O getParameter(String id, IArgumentType<O> parser);

    Map<String, ParseError> getParsingErrors();
    void ifErrorPresent(String id, Predicate<ParseError> predicate, Consumer<ParseError> consumer);

    S getContext();
}
