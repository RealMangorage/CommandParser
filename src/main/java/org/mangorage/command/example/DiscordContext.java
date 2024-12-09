package org.mangorage.command.example;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public final class DiscordContext {

    public static LiteralArgumentBuilder<DiscordContext> literal(String id) {
        return LiteralArgumentBuilder.literal(id);
    }

    public static <T> RequiredArgumentBuilder<DiscordContext, T> argument(String id, ArgumentType<T> type) {
        return RequiredArgumentBuilder.argument(id, type);
    }

    private final MessageReceivedEvent event;

    public DiscordContext(MessageReceivedEvent event) {
        this.event = event;
    }

    public void reply(String message) {
        event.getMessage().reply(message).queue();
    }

    public User getUser() {
        return event.getAuthor();
    }
}
