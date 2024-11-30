package org.mangorage.cmd.core;

public interface IParser<O> {
    O parse(String[] args);
}