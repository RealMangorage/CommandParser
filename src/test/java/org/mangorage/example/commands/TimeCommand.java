package org.mangorage.example.commands;

import org.mangorage.command.api.IAutoRegister;
import org.mangorage.command.api.ICommand;
import org.mangorage.command.impl.CommandAlias;
import org.mangorage.command.impl.argument.ArgumentTypes;
import org.mangorage.command.impl.argument.ParseError;
import org.mangorage.command.impl.misc.Validators;
import org.mangorage.example.AutoRegister;
import org.mangorage.example.DiscordContext;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

@AutoRegister
public final class TimeCommand implements IAutoRegister<AutoRegister, ICommand<DiscordContext>> {
    private int timer = 0;

    @Override
    public Optional<ICommand<DiscordContext>> register(AutoRegister annotation, Consumer<Object> callback) {
        var command = new TimeCommand();
        callback.accept(command);
        return Optional.of(command.create());
    }

    public ICommand<DiscordContext> create() {
        ICommand<DiscordContext> add = DiscordContext.literal("add")
                .executes(s -> {
                    var context = s.getContext();
                    var seconds = s.getParameter("seconds", ArgumentTypes.INT);
                    timer += seconds;
                    context.reply(
                            context.getUser().getGlobalName() + " Added " + seconds + " seconds to the timer!"
                    );
                    return 1;
                })
                .withArgument(
                        DiscordContext.argument("seconds", ArgumentTypes.INT)
                                .onError((s, e) -> {
                                    if (e != ParseError.INVALID) {
                                        s.getContext().reply("Error with Parameter 'seconds', %s".formatted(e));
                                    } else {
                                        s.getContext().reply("Parameter 'seconds' can only be within range 0 - 100");
                                    }
                                })
                                .build()
                )
                .build();

        ICommand<DiscordContext> remove = DiscordContext.literal("remove")
                .executes(s -> {
                    var context = s.getContext();
                    var seconds = s.getParameter("seconds", ArgumentTypes.INT);
                    timer -= seconds;
                    context.reply(
                            context.getUser().getGlobalName() + " Removed " + seconds + " seconds from the timer!"
                    );
                    return 1;
                })
                .withArgument(
                        DiscordContext.argument("seconds", ArgumentTypes.INT)
                                .onError((s, e) -> {
                                    if (e != ParseError.INVALID) {
                                        s.getContext().reply("Error with Parameter 'seconds', %s".formatted(e));
                                    } else {
                                        s.getContext().reply("Parameter 'seconds' can only be within range 0 - 100");
                                    }
                                })
                                .build()
                )
                .build();

        ICommand<DiscordContext> set = DiscordContext.literal("set")
                .executes(s -> {
                    var context = s.getContext();
                    var seconds = s.getParameter("seconds", ArgumentTypes.INT);
                    timer = seconds;
                    context.reply(
                            context.getUser().getGlobalName() + " Set timer to " + seconds + " seconds!"
                    );
                    return 1;
                })
                .withArgument(
                        DiscordContext.argument("seconds", ArgumentTypes.INT)
                                .validate(Validators.intRange(0, 100))
                                .onError((s, e) -> {
                                    if (e != ParseError.INVALID) {
                                        s.getContext().reply("Error with Parameter 'seconds', %s".formatted(e));
                                    } else {
                                        s.getContext().reply("Parameter 'seconds' can only be within range 0 - 100");
                                    }
                                })
                                .build()
                )
                .build();

        ICommand<DiscordContext> info = DiscordContext.literal("info")
                .executes(s -> {
                    var context = s.getContext();
                    context.reply(
                            "Timer has " + timer + " seconds!"
                    );
                    return 1;
                })
                .build();

        return DiscordContext.literal("time")
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
                            info.createCommandAlias("test")
                        )

                )
                .build();
    }
}
