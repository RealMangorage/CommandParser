package org.mangorage.example.commands;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import org.mangorage.command.impl.argument.ArgumentTypes;
import org.mangorage.example.DiscordContext;


public final class TimeCommand {
    private int timer = 0;

    public LiteralArgumentBuilder<DiscordContext> create() {
        var time = DiscordContext.literal("time")
                .then(
                        DiscordContext.literal("set")

                                .then(
                                        DiscordContext.argument("seconds", IntegerArgumentType.integer(0, 100))
                                                .executes(c -> {
                                                    c.getSource().reply("%s set Timer to %s seconds".formatted(c.getSource().getUser().getName(), c.getArgument("seconds", Integer.class)));
                                                    return 1;
                                                })
                                                .then(
                                                        DiscordContext.argument("optional", StringArgumentType.string())
                                                                .executes(c -> {
                                                                    return 1;
                                                                })
                                                                .build()
                                                )
                                                .build()
                                )
                                .build()
                );
        return time;
    }
}
