package org.mangorage.cmd.api;

import org.mangorage.cmd.impl.argument.ParseResult;

import java.util.function.Predicate;

public interface IArgumentType<O> {
    ParseResult<O> parse(Predicate<O> validator, String[] args);
    Class<O> getArgumentClass();
}