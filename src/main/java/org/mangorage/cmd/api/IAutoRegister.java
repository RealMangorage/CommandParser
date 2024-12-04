package org.mangorage.cmd.api;

import java.lang.annotation.Annotation;
import java.util.Optional;
import java.util.function.Consumer;

public interface IAutoRegister<A extends Annotation, T> {
    Optional<T> register(A annotation, Consumer<Object> callback);
}
