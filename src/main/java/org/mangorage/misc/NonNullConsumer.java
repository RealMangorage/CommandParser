package org.mangorage.misc;

import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface NonNullConsumer<T>
{
    void accept(@NotNull T t);
}
