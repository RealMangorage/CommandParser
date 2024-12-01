package org.mangorage.cmd.api;

public interface ICommandDispatcher<S> {
    void register(String id, ICommand<S> command);
    void execute(S context, String[] args);

    default void execute(S context, String args) {
        execute(context, args.split(" "));
    }
}
