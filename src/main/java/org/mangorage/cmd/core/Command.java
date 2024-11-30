package org.mangorage.cmd.core;

import org.mangorage.cmd.core.argument.IArgumentType;
import org.mangorage.cmd.core.context.CommandSourceStack;
import org.mangorage.cmd.Util;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
public final class Command<C, R> {

    public static <C, R> Command.Builder<C, R> literal(Class<C> cClass, Class<R> rClass) {
        return new Builder<>();
    }

    private final BiFunction<C, CommandSourceStack, R> onExecute;
    private final Map<String, Command<C, R>> subCommands;
    private final Map<String, IArgumentType<?>> parameters;

    private Command(final BiFunction<C, CommandSourceStack, R> onExecute, Map<String, Command<C, R>> subCommands, Map<String, IArgumentType<?>> parameters) {
        this.onExecute = onExecute;
        this.subCommands = subCommands;
        this.parameters = parameters;
    }

    public R execute(C globalContext, String[] args) {
        if (args.length > 0) {
            var subCommand = subCommands.get(args[0]);
            if (subCommand != null)
                return subCommand.execute(
                        globalContext,
                        Util.shrinkArray(args)
                );
            else
                return onExecute.apply(
                        globalContext,
                        CommandSourceStack.of(
                                parameters,
                                args
                        )
                );
        } else {
            return onExecute.apply(
                    globalContext,
                    CommandSourceStack.of(
                            parameters,
                            args
                    )
            );
        }
    }

    public static final class Builder<C, R> {
        private BiFunction<C, CommandSourceStack, R> onExecute;
        private final Map<String, Command<C, R>> subCommands = new HashMap<>();
        private final Map<String, IArgumentType<?>> parameters = new HashMap<>();

        private Builder() {}

        public Builder<C, R> executes(BiFunction<C, CommandSourceStack, R> onExecute) {
            this.onExecute = onExecute;
            return this;
        }

        public Builder<C, R> subCommand(String id, Command<C, R> command) {
            this.subCommands.put(id, command);
            return this;
        }

        public <P> Builder<C, R> withParameter(String id, IArgumentType<P> parameterType) {
            this.parameters.put(id, parameterType);
            return this;
        }

        public Command<C, R> build(R defaultValue) {
            if (this.onExecute == null) executes((c1, c2) -> defaultValue);
            return new Command<>(onExecute, subCommands, parameters);
        }
    }
}
