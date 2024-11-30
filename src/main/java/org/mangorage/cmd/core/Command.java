package org.mangorage.cmd.core;

import org.mangorage.cmd.core.context.Context;
import org.mangorage.cmd.Util;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Consumer;

public final class Command<C, R> {

    public static <C, R> Command<C, R> of(BiFunction<C, Context, R> onExecute) {
        return new Command<>(onExecute);
    }

    private final BiFunction<C, Context, R> onExecute;
    private final Map<String, Command<C, R>> subCommands = new HashMap<>();

    private Command(BiFunction<C, Context, R> onExecute) {
        this.onExecute = onExecute;
    }


    public Command<C, R> subCommand(String subCmd, Command<C, R> command) {
        this.subCommands.put(subCmd, command);
        return this;
    }

    public R execute(C globalContext, String[] args) {
        if (args.length > 0) {
            var subCommand = subCommands.get(args[0]);
            if (subCommand != null)
                return subCommand.execute(
                        globalContext,
                        Util.popArray(args)
                );
            else
                return onExecute.apply(
                        globalContext,
                        Context.of(args)
                );
        } else {
            return onExecute.apply(
                    globalContext,
                    Context.of(
                            args
                    )
            );
        }
    }
}
