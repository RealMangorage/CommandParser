package org.mangorage.cmd.core.impl;

import org.mangorage.cmd.core.IArgumentType;

import java.util.function.Function;

public final class ArgumentTypeImpl<O> implements IArgumentType<O> {
    private final Class<O> type;
    private final Function<String[], O> function;

    public ArgumentTypeImpl(Class<O> oClass, Function<String[], O> function) {
        this.type = oClass;
        this.function = function;
    }

    @Override
    public O parse(String[] args) {
        return function.apply(args);
    }

    @Override
    public Class<O> getType() {
        return type;
    }
}
