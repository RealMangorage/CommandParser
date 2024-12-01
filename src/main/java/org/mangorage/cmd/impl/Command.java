package org.mangorage.cmd.impl;

import org.mangorage.cmd.api.ICommand;
import org.mangorage.cmd.api.ICommandSourceStack;
import org.mangorage.cmd.api.IArgumentType;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public final class Command<S> implements ICommand<S> {

    public static <S> Command.Builder<S> literal(Class<S> contextClass) {
        return new Builder<>();
    }

    private final Consumer<ICommandSourceStack<S>> onExecute;
    private final Map<String, ICommand<S>> subCommands;
    private final Map<String, IArgumentType<?>> parameters;

    private Command(final Consumer<ICommandSourceStack<S>> onExecute, Map<String, ICommand<S>> subCommands, Map<String, IArgumentType<?>> parameters) {
        this.onExecute = onExecute;
        this.subCommands = subCommands;
        this.parameters = parameters;
    }

    public void execute(ICommandSourceStack<S> commandSourceStack) {
        var args = commandSourceStack.getRemainingArgs();
        commandSourceStack.updateParameters(parameters);
        if (args.length > 0) {
            var subCommand = subCommands.get(args[0]);
            if (subCommand != null) {
                commandSourceStack.shrinkArgs();
                subCommand.execute(commandSourceStack);
            }
            else
                onExecute.accept(commandSourceStack);
        } else {
            onExecute.accept(commandSourceStack);
        }
    }

    @Override
    public Map<String, IArgumentType<?>> getParameters() {
        return Map.copyOf(parameters);
    }

    public static final class Builder<S> {
        private Consumer<ICommandSourceStack<S>> onExecute;
        private final Map<String, ICommand<S>> subCommands = new HashMap<>();
        private final Map<String, IArgumentType<?>> parameters = new HashMap<>();

        private Builder() {}

        public Builder<S> executes(Consumer<ICommandSourceStack<S>> onExecute) {
            this.onExecute = onExecute;
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
            if (this.onExecute == null) executes(s -> {});
            return new Command<>(onExecute, subCommands, parameters);
        }
    }
}
