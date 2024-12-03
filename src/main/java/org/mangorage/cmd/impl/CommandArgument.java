package org.mangorage.cmd.impl;

import org.mangorage.cmd.api.IArgument;
import org.mangorage.cmd.api.IArgumentType;
import org.mangorage.cmd.api.ICommandSourceStack;
import org.mangorage.cmd.impl.argument.ParseError;

import java.util.function.BiConsumer;

public final class CommandArgument<S> implements IArgument<S> {

    public static <T, S> ArgumentBuilder<T, S> literal(String id, IArgumentType<T> argumentType, Class<S> sClass) {
        return new ArgumentBuilder<>(id, argumentType);
    }

    private final String id;
    private final IArgumentType<?> type;
    private final BiConsumer<ICommandSourceStack<S>, ParseError> consumer;

    private CommandArgument(String id, IArgumentType<?> type, BiConsumer<ICommandSourceStack<S>, ParseError> consumer) {
        this.id = id;
        this.type = type;
        this.consumer = consumer;
    }

    @Override
    public IArgumentType<?> getType() {
        return type;
    }

    @Override
    public BiConsumer<ICommandSourceStack<S>, ParseError> getErrorConsumer() {
        return consumer;
    }

    @Override
    public String getId() {
        return id;
    }

    public static final class ArgumentBuilder<T, S> {
        private final String id;
        private final IArgumentType<T> type;
        private BiConsumer<ICommandSourceStack<S>, ParseError> consumer;

        private ArgumentBuilder(String id, IArgumentType<T> argumentType) {
            this.id = id;
            this.type = argumentType;
        }

        public ArgumentBuilder<T, S> onError(BiConsumer<ICommandSourceStack<S>, ParseError> consumer) {
            this.consumer = consumer;
            return this;
        }

        public CommandArgument<S> build() {
            return new CommandArgument<>(
                    id,
                    type,
                    consumer
            );
        }
    }
}
