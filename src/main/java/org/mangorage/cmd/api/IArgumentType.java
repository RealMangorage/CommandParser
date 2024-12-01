package org.mangorage.cmd.api;

import org.mangorage.cmd.impl.argument.ParseResult;

public interface IArgumentType<O> {
    ParseResult<O> parse(String[] args);
    Class<O> getType();
}