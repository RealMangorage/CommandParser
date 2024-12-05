package org.mangorage.example.commands;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.User;
import org.mangorage.example.DiscordContext;
import org.mangorage.example.args.UserArgumentType;


public final class TimeCommand {
    private int timer = 0;

    public LiteralArgumentBuilder<DiscordContext> create(JDA jda) {
        var time = DiscordContext.literal("time")
                .then(
                        DiscordContext.literal("set")
                                .then(
                                        DiscordContext.argument("seconds", IntegerArgumentType.integer(0, 20))
                                                .executes(c -> {
                                                    c.getSource().reply(
                                                            """
                                                            Set Timer to %s for %s
                                                            """.formatted(
                                                                    c.getArgument("seconds", Integer.class),
                                                                    c.getSource().getUser().getName()
                                                            )
                                                    );
                                                    return 1;
                                                })
                                                .build()
                                )
                                .then(
                                        DiscordContext.argument("user", UserArgumentType.globalUser(jda))
                                                .then(
                                                        DiscordContext.argument("seconds", IntegerArgumentType.integer(0, 20))
                                                                .executes(c -> {
                                                                    c.getSource().reply(
                                                                            """
                                                                            Set Timer to %s for %s
                                                                            """.formatted(
                                                                                    c.getArgument("seconds", Integer.class),
                                                                                    c.getArgument("user", User.class).getName()
                                                                            )
                                                                    );
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
