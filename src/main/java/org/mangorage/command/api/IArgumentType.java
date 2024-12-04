package org.mangorage.command.api;

import org.mangorage.command.impl.argument.ParseResult;

import java.util.function.Predicate;

public interface IArgumentType<O> {
    ParseResult<O> parse(Predicate<O> validator, String[] args);
    Class<O> getArgumentClass();
}