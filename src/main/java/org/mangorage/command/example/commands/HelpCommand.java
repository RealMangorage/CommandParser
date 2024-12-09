package org.mangorage.command.example.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import org.mangorage.command.example.DiscordContext;

public class HelpCommand {
    public LiteralArgumentBuilder<DiscordContext> create() {
        var time = DiscordContext.literal("help")
                .executes(c -> {
                    return 1;
                });
        return time;
    }
}
