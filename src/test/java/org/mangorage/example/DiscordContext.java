package org.mangorage.example;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.mangorage.command.api.IArgumentType;
import org.mangorage.command.impl.Command;
import org.mangorage.command.impl.CommandArgument;

public final class DiscordContext {

    public static Command.CommandBuilder<DiscordContext> literal(String id) {
        return Command.literal(id, DiscordContext.class);
    }

    public static <T> CommandArgument.ArgumentBuilder<T, DiscordContext> argument(String id, IArgumentType<T> type) {
        return CommandArgument.literal(id, type, DiscordContext.class);
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
