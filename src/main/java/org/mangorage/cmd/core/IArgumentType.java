package org.mangorage.cmd.core;

public interface IArgumentType<O> {
    O parse(String[] args);
    Class<O> getType();
}