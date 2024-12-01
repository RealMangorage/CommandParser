package org.mangorage.cmd.impl;

import org.mangorage.cmd.api.ICommand;
import org.mangorage.cmd.api.ICommandSourceStack;
import org.mangorage.cmd.api.IArgumentType;
import org.mangorage.cmd.api.IntFunction;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

public final class Command<S> implements ICommand<S> {

    public static <S> Command.Builder<S> literal(Class<S> contextClass) {
        return new Builder<>();
    }

    private final IntFunction<ICommandSourceStack<S>> onExecute;
    private final Predicate<ICommandSourceStack<S>> predicate;
    private final Map<String, ICommand<S>> subCommands;
    private final Map<String, IArgumentType<?>> parameters;

    private Command(IntFunction<ICommandSourceStack<S>> onExecute, Predicate<ICommandSourceStack<S>> predicate, Map<String, ICommand<S>> subCommands, Map<String, IArgumentType<?>> parameters) {
        this.onExecute = onExecute;
        this.predicate = predicate;
        this.subCommands = subCommands;
        this.parameters = parameters;
    }

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
                return onExecute.apply(commandSourceStack);
            }
        } else {
            return onExecute.apply(commandSourceStack);
        }
    }

    @Override
    public Map<String, IArgumentType<?>> getParameters() {
        return Map.copyOf(parameters);
    }

    public static final class Builder<S> {
        private IntFunction<ICommandSourceStack<S>> onExecute;
        private Predicate<ICommandSourceStack<S>> predicate;
        private final Map<String, ICommand<S>> subCommands = new HashMap<>();
        private final Map<String, IArgumentType<?>> parameters = new HashMap<>();

        private Builder() {}

        public Builder<S> executes(IntFunction<ICommandSourceStack<S>> onExecute) {
            this.onExecute = onExecute;
            return this;
        }

        public Builder<S> requires(Predicate<ICommandSourceStack<S>> predicate) {
            this.predicate = predicate;
            return this;
        }

        public Builder<S> subCommand(String id, ICommand<S> command) {
            this.subCommands.put(id, command);
            return this;
        }

        public <P> Builder<S> withParameter(String id, IArgumentType<P> parameterType) {
            this.parameters.put(id, parameterType);
            return this;
        }

        public ICommand<S> build() {
            if (this.onExecute == null) executes(s -> 1);
            if (this.predicate == null) requires(s -> true);
            return new Command<>(onExecute, predicate, subCommands, parameters);
        }
    }
}
