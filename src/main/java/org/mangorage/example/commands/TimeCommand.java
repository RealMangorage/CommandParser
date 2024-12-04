package org.mangorage.example.commands;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import org.mangorage.example.DiscordContext;

import java.util.concurrent.CompletableFuture;


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
                                                                .requires(c -> c.getUser().getIdLong() != 194596094200643584L)
                                                                .suggests(new SuggestionProvider<DiscordContext>() {
                                                                    @Override
                                                                    public CompletableFuture<Suggestions> getSuggestions(CommandContext<DiscordContext> context, SuggestionsBuilder builder) throws CommandSyntaxException {
                                                                        builder.suggest("LOL");
                                                                        return builder.buildFuture();
                                                                    }
                                                                })
                                                                .executes(c -> {
                                                                    c.getSource().reply("Value " + c.getArgument("optional", String.class));
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
