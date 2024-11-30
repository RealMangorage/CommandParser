package org.mangorage.cmd.core.argument;

public interface IArgumentType<O> {
    ParseResult<O> parse(String[] args);
    Class<O> getType();
}