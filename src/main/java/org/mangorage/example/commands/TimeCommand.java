package org.mangorage.example.commands;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.User;
import org.mangorage.example.DiscordContext;
import org.mangorage.example.args.UserArgumentType;

import java.util.function.BiConsumer;
import java.util.function.Consumer;


public final class TimeCommand {




    private LiteralCommandNode<DiscordContext> of(String id, JDA jda, BiConsumer<CommandContext<DiscordContext>, Boolean> consumer) {
        return  DiscordContext.literal(id)
                .then(
                        DiscordContext.argument("seconds", IntegerArgumentType.integer(0, 20))
                                .executes(c -> {
                                    consumer.accept(c, false);
                                    return 1;
                                })
                                .build()
                )
                .then(
                        DiscordContext.argument("user", UserArgumentType.globalUser(jda))
                                .then(
                                        DiscordContext.argument("seconds", IntegerArgumentType.integer(0, 20))
                                                .executes(c -> {
                                                    consumer.accept(c, true);
                                                    return 1;
                                                })
                                                .build()
                                )
                                .build()
                )
                .build();
    }

    public LiteralArgumentBuilder<DiscordContext> create(JDA jda) {
        var time = DiscordContext.literal("time")
                .then(
                        of("set", jda, (c, b) -> {
                            c.getSource().reply(
                                    """
                                    Set Timer to %s seconds for %s
                                    """.formatted(
                                            c.getArgument("seconds", Integer.class),
                                            !b ? c.getSource().getUser().getName() : c.getArgument("user", User.class).getName()
                                    )

                            );
                        })
                )
                .then(
                        of("remove", jda, (c, b) -> {
                            c.getSource().reply(
                                """
                                Removed %s seconds from Timer for %s
                                """.formatted(
                                        c.getArgument("seconds", Integer.class),
                                        !b ? c.getSource().getUser().getName() : c.getArgument("user", User.class).getName()
                                )
                            );
                        })
                )
                .then(
                        of("add", jda, (c, b) -> {
                            c.getSource().reply(
                                """
                                Added %s seconds from Timer for %s
                                """.formatted(
                                        c.getArgument("seconds", Integer.class),
                                        !b ? c.getSource().getUser().getName() : c.getArgument("user", User.class).getName()
                                )
                            );
                        })
                );
        return time;
    }
}
