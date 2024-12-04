package org.mangorage.command.impl;

import org.mangorage.command.api.IArgument;
import org.mangorage.command.api.ICommand;
import org.mangorage.command.api.ICommandSourceStack;
import org.mangorage.command.api.IntFunction;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

public final class Command<S> implements ICommand<S> {

    public static <S> CommandBuilder<S> literal(String id, Class<S> contextClass) {
        return new CommandBuilder<>(id);
    }

    private final String id;
    private final IntFunction<ICommandSourceStack<S>> onExecute;
    private final Predicate<ICommandSourceStack<S>> predicate;
    private final Map<String, ICommand<S>> subCommands;
    private final Map<String, IArgument<S>> parameters;

    private Command(String id, IntFunction<ICommandSourceStack<S>> onExecute, Predicate<ICommandSourceStack<S>> predicate, Map<String, ICommand<S>> subCommands, Map<String, IArgument<S>> parameters) {
        this.id = id;
        this.onExecute = onExecute;
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
            return 2;
        }
    }

    @Override
    public Map<String, IArgument<S>> getArguments() {
        return Map.copyOf(parameters);
    }

    @Override
    public ICommand<S> createCommandAlias(String id) {
        return CommandAlias.of(id, this);
    }

    @Override
    public String getId() {
        return id;
    }

    public static final class CommandBuilder<S> {
        private final String id;
        private IntFunction<ICommandSourceStack<S>> onExecute;
        private Predicate<ICommandSourceStack<S>> predicate;
        private final Map<String, ICommand<S>> subCommands = new HashMap<>();
        private final Map<String, IArgument<S>> parameters = new HashMap<>();

        private CommandBuilder(String id) {
            this.id = id;
        }

        public CommandBuilder<S> executes(IntFunction<ICommandSourceStack<S>> onExecute) {
            this.onExecute = onExecute;
            return this;
        }

        public CommandBuilder<S> requires(Predicate<ICommandSourceStack<S>> predicate) {
            this.predicate = predicate;
            return this;
        }

        public CommandBuilder<S> subCommand(ICommand<S> command) {
            this.subCommands.put(command.getId(), command);
            return this;
        }

        public CommandBuilder<S> subCommands(Collection<ICommand<S>> commands) {
            for (ICommand<S> command : commands) {
                subCommand(command);
            }
            return this;
        }

        public CommandBuilder<S> withArgument(IArgument<S> argument) {
            this.parameters.put(argument.getId(), argument);
            return this;
        }

        public CommandBuilder<S> withArguments(Collection<IArgument<S>> arguments) {
            for (IArgument<S> argument : arguments) {
                withArgument(argument);
            }
            return this;
        }


        public ICommand<S> build() {
            if (this.onExecute == null) executes(s -> 1);
            if (this.predicate == null) requires(s -> true);
            return new Command<>(
                    id,
                    onExecute,
                    predicate,
                    subCommands,
                    parameters
            );
        }
    }
}
