package org.mangorage.cmd.api;

public interface ICommandRegistrar<S> {
    S create();
}
