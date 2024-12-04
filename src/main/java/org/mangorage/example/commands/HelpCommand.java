package org.mangorage.example.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import org.mangorage.example.DiscordContext;

public class HelpCommand {
    public LiteralArgumentBuilder<DiscordContext> create() {
        var time = DiscordContext.literal("help")
                .executes(c -> {
                    return 1;
                });
        return time;
    }
}
