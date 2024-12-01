package org.mangorage.cmd.impl.argument;

import org.mangorage.cmd.api.IArgumentType;

import java.util.function.Function;

public final class ArgumentTypeImpl<O> implements IArgumentType<O> {
    private final Class<O> type;
    private final Function<String[], ParseResult<O>> function;

    public ArgumentTypeImpl(Class<O> oClass, Function<String[], ParseResult<O>> function) {
        this.type = oClass;
        this.function = function;
    }

    @Override
    public ParseResult<O> parse(String[] args) {
        return function.apply(args);
    }

    @Override
    public Class<O> getType() {
        return type;
    }
}
