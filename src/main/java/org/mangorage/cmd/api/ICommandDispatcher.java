package org.mangorage.cmd.api;

public interface ICommandDispatcher<S> {
    void register(String id, ICommand<S> command);

    /**
     * Defgult Int Return Types:
     * -1 INVALID
     * 0 NO PERMS
     * 1 PASS
     */

    int execute(S context, String[] args);

    default void execute(S context, String args) {
        execute(context, args.split(" "));
    }
}
