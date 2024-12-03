package org.mangorage.cmd.api;

import org.mangorage.cmd.impl.argument.ParseError;

import java.util.function.BiConsumer;
import java.util.function.Predicate;

public interface IArgument<S> extends IdHolder {
    IArgumentType<?> getType();
    Predicate<?> getPredicate();
    BiConsumer<ICommandSourceStack<S>, ParseError> getErrorConsumer();
}
