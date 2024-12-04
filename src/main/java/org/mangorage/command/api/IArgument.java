package org.mangorage.command.api;

import org.mangorage.command.impl.argument.ParseError;

import java.util.function.BiConsumer;
import java.util.function.Predicate;

public interface IArgument<S> extends IdHolder {
    IArgumentType<?> getType();
    Predicate<?> getPredicate();
    BiConsumer<ICommandSourceStack<S>, ParseError> getErrorConsumer();
}
