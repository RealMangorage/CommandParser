package org.mangorage.command.api;

public interface IExecutable<T> {
    int execute(T t);
}
