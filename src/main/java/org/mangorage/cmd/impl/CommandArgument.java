package org.mangorage.cmd.impl;

import org.mangorage.cmd.api.IArgument;
import org.mangorage.cmd.api.IArgumentType;
import org.mangorage.cmd.api.ICommandSourceStack;
import org.mangorage.cmd.impl.argument.ParseError;

import java.util.function.BiConsumer;
import java.util.function.Predicate;

public final class CommandArgument<S> implements IArgument<S> {

    public static <T, S> ArgumentBuilder<T, S> literal(String id, IArgumentType<T> argumentType, Class<S> sClass) {
        return new ArgumentBuilder<>(id, argumentType);
    }

    private final String id;
    private final IArgumentType<?> type;
    private final Predicate<?> predicate;
    private final BiConsumer<ICommandSourceStack<S>, ParseError> consumer;

    private CommandArgument(String id, IArgumentType<?> type, Predicate<?> predicate, BiConsumer<ICommandSourceStack<S>, ParseError> consumer) {
        this.id = id;
        this.type = type;
        this.predicate = predicate;
        this.consumer = consumer;
    }

    @Override
    public IArgumentType<?> getType() {
        return type;
    }

    @Override
    public Predicate<?> getPredicate() {
        return predicate;
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
        private Predicate<T> predicate;
        private BiConsumer<ICommandSourceStack<S>, ParseError> consumer;

        private ArgumentBuilder(String id, IArgumentType<T> argumentType) {
            this.id = id;
            this.type = argumentType;
        }

        public ArgumentBuilder<T, S> validate(Predicate<T> predicate) {
            this.predicate = predicate;
            return this;
        }

        public ArgumentBuilder<T, S> onError(BiConsumer<ICommandSourceStack<S>, ParseError> consumer) {
            this.consumer = consumer;
            return this;
        }

        public CommandArgument<S> build() {
            if (predicate == null) validate(o -> true); // default
            return new CommandArgument<>(
                    id,
                    type,
                    predicate,
                    consumer
            );
        }
    }
}
