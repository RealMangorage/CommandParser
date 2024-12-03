package org.mangorage.cmd.impl;

import org.mangorage.cmd.api.ICommand;
import org.mangorage.cmd.api.ICommandSourceStack;
import org.mangorage.cmd.api.IArgumentType;
import org.mangorage.cmd.api.IntFunction;
import org.mangorage.cmd.impl.context.CommandSourceStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;

public final class Command<S> implements ICommand<S> {

    public static <S> Command.Builder<S> literal(String id, Class<S> contextClass) {
        return new Builder<>(id);
    }

    private final String id;
    private final IntFunction<ICommandSourceStack<S>> onExecute;
    private final Consumer<ICommandSourceStack<S>> onError;
    private final Predicate<ICommandSourceStack<S>> predicate;
    private final Map<String, ICommand<S>> subCommands;
    private final Map<String, IArgumentType<?>> parameters;

    private Command(String id, IntFunction<ICommandSourceStack<S>> onExecute, Consumer<ICommandSourceStack<S>> onError, Predicate<ICommandSourceStack<S>> predicate, Map<String, ICommand<S>> subCommands, Map<String, IArgumentType<?>> parameters) {
        this.id = id;
        this.onExecute = onExecute;
        this.onError = onError;
        this.predicate = predicate;
        this.subCommands = subCommands;
        this.parameters = parameters;
    }

    @Override
    public int execute(ICommandSourceStack<S> commandSourceStack) {
        var args = commandSourceStack.getRemainingArgs();
        commandSourceStack.updateParameters(parameters);
        if (!predicate.test(commandSourceStack)) return 0;
        if (args.length > 0) {
            var subCommand = subCommands.get(args[0]);
            if (subCommand != null) {
                commandSourceStack.shrinkArgs();
                return subCommand.execute(commandSourceStack);
            } else {
                return finalizeExecute(commandSourceStack);
            }
        } else {
            return finalizeExecute(commandSourceStack);
        }
    }

    private int finalizeExecute(ICommandSourceStack<S> commandSourceStack) {
        try {
            return onExecute.apply(commandSourceStack);
        } catch (Throwable exception) {
            onError.accept(commandSourceStack);
            return 2;
        }
    }

    @Override
    public Map<String, IArgumentType<?>> getParameters() {
        return Map.copyOf(parameters);
    }

    @Override
    public String getId() {
        return id;
    }

    public static final class Builder<S> {
        private final String id;
        private IntFunction<ICommandSourceStack<S>> onExecute;
        private Consumer<ICommandSourceStack<S>> onError;
        private Predicate<ICommandSourceStack<S>> predicate;
        private final Map<String, ICommand<S>> subCommands = new HashMap<>();
        private final Map<String, IArgumentType<?>> parameters = new HashMap<>();

        private Builder(String id) {
            this.id = id;
        }

        public Builder<S> executes(IntFunction<ICommandSourceStack<S>> onExecute) {
            this.onExecute = onExecute;
            return this;
        }

        public Builder<S> onError(Consumer<ICommandSourceStack<S>> onError) {
            this.onError = onError;
            return this;
        }

        public Builder<S> requires(Predicate<ICommandSourceStack<S>> predicate) {
            this.predicate = predicate;
            return this;
        }

        public Builder<S> subCommand(ICommand<S> command) {
            this.subCommands.put(command.getId(), command);
            return this;
        }

        public Builder<S> subCommands(List<ICommand<S>> commands) {
            for (ICommand<S> command : commands) {
                this.subCommands.put(command.getId(), command);
            }
            return this;
        }

        public <P> Builder<S> withParameter(String id, IArgumentType<P> parameterType) {
            this.parameters.put(id, parameterType);
            return this;
        }

        public ICommand<S> build() {
            if (this.onExecute == null) executes(s -> 1);
            if (this.onError == null) onError(s -> {});
            if (this.predicate == null) requires(s -> true);
            return new Command<>(
                    id,
                    onExecute,
                    onError,
                    predicate,
                    subCommands,
                    parameters
            );
        }
    }
}
