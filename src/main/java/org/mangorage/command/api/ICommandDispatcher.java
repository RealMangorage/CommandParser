package org.mangorage.command.api;

import java.lang.annotation.Annotation;
import java.util.function.Consumer;

public interface ICommandDispatcher<S> {
    void register(ICommand<S> command);

    /**
     * Defgult Int Return Types:
     * -1 INVALID
     * 0 NO PERMS
     * 1 PASS
     * 2 ERROR
     */

    int execute(S context, String[] args);

    default int execute(S context, String args) {
        return execute(context, args.split(" "));
    }

    <T extends Annotation> void autoRegister(Class<T> annotationClazz, Consumer<Object> consumer);
}
