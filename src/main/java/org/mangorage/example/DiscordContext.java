package org.mangorage.example;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public final class DiscordContext {

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
