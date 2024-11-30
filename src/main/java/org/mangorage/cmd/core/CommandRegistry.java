package org.mangorage.cmd.core;

import org.mangorage.cmd.Util;
import org.mangorage.cmd.core.context.CommandResult;
import org.mangorage.cmd.core.argument.ArgumentTypes;

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
                return cmd.execute(globalContext, Util.shrinkArray(args));
        }
        return defaultReturn;
    }

    public R execute(C globalContext, String command) {
        return execute(globalContext, command.split(" "));
    }

    public record GlobalContext(String player) {}

    public static void main(String[] args) {
            CommandRegistry<GlobalContext, CommandResult> registry = create(CommandResult.INVALID_COMMAND);


            Command<GlobalContext, CommandResult> time = Util.literal()
                    .subCommand("add",
                            Util.literal()
                                    .executes((a, b) -> {
                                        System.out.println("Added " + b.getParameter("seconds", ArgumentTypes.INT));
                                        System.out.println("Unit " + b.getOptionalParameter("unit", ArgumentTypes.INT).orElse(0));
                                        System.out.println("Who Executed " + a.player());
                                        return CommandResult.PASS;
                                    })
                                    .withParameter("seconds", ArgumentTypes.INT)
                                    .withParameter("unit", ArgumentTypes.INT)
                                    .build(CommandResult.PASS)
                            )
                    .build(CommandResult.PASS);


            registry.register("time", time);

            System.out.println(
                    registry.execute(
                            new GlobalContext("mangorage"),
                            "time",
                            "add",
                            "10900",
                            "1"
                    )
            );



    }
}
