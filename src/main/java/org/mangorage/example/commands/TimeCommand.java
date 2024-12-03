package org.mangorage.example.commands;

import org.mangorage.cmd.api.ICommand;
import org.mangorage.cmd.api.ICommandSourceStack;
import org.mangorage.cmd.impl.Command;
import org.mangorage.cmd.impl.CommandAlias;
import org.mangorage.cmd.impl.argument.ArgumentTypes;
import org.mangorage.cmd.impl.argument.ParseError;
import org.mangorage.example.DiscordContext;
import org.mangorage.example.api.ICommandRegistrar;

import java.util.List;

public final class TimeCommand implements ICommandRegistrar<ICommand<DiscordContext>> {

    private int timer = 0;

    @Override
    public ICommand<DiscordContext> create() {
        ICommand<DiscordContext> add = Command.literal("add", DiscordContext.class)
                .onError(s -> {
                    s.ifErrorPresent("seconds", e -> e == ParseError.INCOMPLETE, e -> {
                        s.getContext().reply("""
                                Missing Parameter: seconds
                                  -> time add/remove/set <seconds>
                                """);
                    });
                    s.ifErrorPresent("seconds", e -> e == ParseError.MALFORMED, e -> {
                        s.getContext().reply("""
                                Malformed Parameter: seconds
                                  -> Expected Integer, got %s
                                """.formatted(s.getPreviousRemainingArgs()[0]));
                    });
                })
                .executes(s -> {
                    var context = s.getContext();
                    var seconds = s.getParameter("seconds", ArgumentTypes.INT);
                    timer += seconds;
                    context.reply(
                            context.getUser().getGlobalName() + " Added " + seconds + " seconds to the timer!"
                    );
                    return 1;
                })
                .withParameter("seconds", ArgumentTypes.INT)
                .build();

        ICommand<DiscordContext> remove = Command.literal("remove", DiscordContext.class)
                .onError(s -> {
                    s.ifErrorPresent("seconds", e -> e == ParseError.INCOMPLETE, e -> {
                        s.getContext().reply("""
                                Missing Parameter: seconds
                                  -> time add/remove/set <seconds>
                                """);
                    });
                    s.ifErrorPresent("seconds", e -> e == ParseError.MALFORMED, e -> {
                        s.getContext().reply("""
                                Malformed Parameter: seconds
                                  -> Expected Integer, got %s
                                """.formatted(s.getPreviousRemainingArgs()[0]));
                    });
                })
                .executes(s -> {
                    var context = s.getContext();
                    var seconds = s.getParameter("seconds", ArgumentTypes.INT);
                    timer -= seconds;
                    context.reply(
                            context.getUser().getGlobalName() + " Removed " + seconds + " seconds from the timer!"
                    );
                    return 1;
                })
                .withParameter("seconds", ArgumentTypes.INT)
                .build();

        ICommand<DiscordContext> set = Command.literal("set", DiscordContext.class)
                .onError(s -> {
                    s.ifErrorPresent("seconds", e -> e == ParseError.INCOMPLETE, e -> {
                        s.getContext().reply("""
                                Missing Parameter: seconds
                                  -> time add/remove/set <seconds>
                                """);
                    });
                    s.ifErrorPresent("seconds", e -> e == ParseError.MALFORMED, e -> {
                        s.getContext().reply("""
                                Malformed Parameter: seconds
                                  -> Expected Integer, got %s
                                """.formatted(s.getPreviousRemainingArgs()[0]));
                    });
                })
                .executes(s -> {
                    var context = s.getContext();
                    var seconds = s.getParameter("seconds", ArgumentTypes.INT);
                    timer = seconds;
                    context.reply(
                            context.getUser().getGlobalName() + " Set timer to " + seconds + " seconds!"
                    );
                    return 1;
                })
                .withParameter("seconds", ArgumentTypes.INT)
                .build();

        ICommand<DiscordContext> info = Command.literal("info", DiscordContext.class)
                .requires(s -> false)
                .executes(s -> {
                    var context = s.getContext();
                    context.reply(
                            "Timer has " + timer + " seconds!"
                    );
                    return 1;
                })
                .build();

        ICommand<DiscordContext> test = CommandAlias.of("test", set);

        return Command.literal("time", DiscordContext.class)
                .executes(s -> {
                    s.getContext().reply(
                            """
                            Usage:
                                time add/remove/set <seconds:int>
                                time info
                            """
                    );
                    return 1;
                })
                .subCommands(
                        List.of(
                                add,
                                remove,
                                set,
                                info,
                                test
                        )
                )
                .build();
    }
}
