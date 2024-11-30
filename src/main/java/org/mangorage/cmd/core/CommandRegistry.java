package org.mangorage.cmd.core;

import org.mangorage.cmd.Util;
import org.mangorage.cmd.core.context.CommandResult;
import org.mangorage.cmd.core.impl.ParserList;

import java.util.HashMap;
import java.util.Map;

public final class CommandRegistry<C, R> {
    public static <C, R> CommandRegistry<C, R> create(R defaultValue) {
        return new CommandRegistry<>(defaultValue);
    }

    private final Map<String, Command<C, R>> commandMap = new HashMap<>();

    private final R defaultReturn;

    private CommandRegistry(R defaultReturn) {
        this.defaultReturn = defaultReturn;
    }

    public void register(String id, Command<C, R> command) {
        commandMap.put(id, command);
    }

    public R execute(C globalContext, String... args) {
        if (args.length >= 1) {
            var cmd = commandMap.get(args[0]);
            if (cmd != null)
                return cmd.execute(globalContext, Util.popArray(args));
        }
        return defaultReturn;
    }

    record GlobalContext(String player) {}

    public static void main(String[] args) {

        CommandRegistry<GlobalContext, CommandResult> registry = create(CommandResult.INVALID_COMMAND);


        Command<GlobalContext, CommandResult> add = Command.of((g, p) -> {
            System.out.println("Added " + p.getObject(ParserList.INT));
            return CommandResult.PASS;
        });

        Command<GlobalContext, CommandResult> remove = Command.of((g, p) -> {
            System.out.println("Removed " + p.getObject(ParserList.INT));
            return CommandResult.PASS;
        });


        System.out.println(
                registry.execute(
                        new GlobalContext("mangorage"),
                        "time",
                        "add",
                        "10900"
                )
        );

        registry.register("time", Command.<GlobalContext, CommandResult>of((g, p) -> CommandResult.PASS)
                .subCommand("add", add)
                .subCommand("remove", remove));

        System.out.println(
                registry.execute(
                        new GlobalContext("mangorage"),
                        "time",
                        "add",
                        "10900"
                )
        );

        System.out.println(
                registry.execute(
                        new GlobalContext("mangorage"),
                        "time",
                        "remove",
                        "10900"
                )
        );
    }
}
