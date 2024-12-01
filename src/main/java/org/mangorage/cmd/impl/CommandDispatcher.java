package org.mangorage.cmd.impl;

import org.mangorage.cmd.Util;
import org.mangorage.cmd.api.ICommand;
import org.mangorage.cmd.api.ICommandDispatcher;
import org.mangorage.cmd.impl.argument.ArgumentTypes;
import org.mangorage.cmd.impl.context.CommandSourceStack;

import java.util.HashMap;
import java.util.Map;

public final class CommandDispatcher<S> implements ICommandDispatcher<S> {
    public static <S> CommandDispatcher<S> create(Class<S> contextClass) {
        return new CommandDispatcher<>();
    }

    private final Map<String, ICommand<S>> commandMap = new HashMap<>();

    private CommandDispatcher() {}

    @Override
    public void register(String id, ICommand<S> command) {
        commandMap.put(id, command);
    }

    public void execute(S context, String... args) {
        if (args.length >= 1) {
            var cmd = commandMap.get(args[0]);
            if (cmd != null)
                cmd.execute(
                        CommandSourceStack.of(
                                context,
                                Util.shrinkArray(args)
                        )
                );
        }
    }

    public record GlobalContext(String player) {}

    public static void main(String[] args) {
            ICommandDispatcher<GlobalContext> dispatcher = create(GlobalContext.class);


            ICommand<GlobalContext> time = Util.literal()
                    .subCommand("add", Util.literal()
                            .executes(s -> {
                                System.out.println(s.getContext().player() + " Added " + s.getParameter("seconds", ArgumentTypes.INT));
                            })
                            .withParameter("seconds", ArgumentTypes.INT)
                            .build()
                    )
                    .build();

            dispatcher.register("time", time);

            dispatcher.execute(
                    new GlobalContext("MangoRage"),
                    "time add 1000"
            );

    }
}
