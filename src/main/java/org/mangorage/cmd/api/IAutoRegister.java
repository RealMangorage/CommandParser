package org.mangorage.cmd.api;

import java.util.Optional;

public interface IAutoRegister<A, S> {
    Optional<ICommand<S>> register(A annotation, ICommandRegistrar<?> object);
}
