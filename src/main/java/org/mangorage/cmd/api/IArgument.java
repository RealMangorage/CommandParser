package org.mangorage.cmd.api;

import org.mangorage.cmd.impl.argument.ParseError;

import java.util.function.BiConsumer;

public interface IArgument<S> extends IdHolder {
    IArgumentType<?> getType();
    BiConsumer<ICommandSourceStack<S>, ParseError> getErrorConsumer();
}
