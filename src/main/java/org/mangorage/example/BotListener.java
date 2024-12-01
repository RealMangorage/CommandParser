package org.mangorage.example;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.SubscribeEvent;
import org.mangorage.cmd.api.ICommandDispatcher;

public final class BotListener {
    private final ICommandDispatcher<DiscordContext> dispatcher;

    public BotListener(ICommandDispatcher<DiscordContext> dispatcher) {
        this.dispatcher = dispatcher;
    }

    @SubscribeEvent
    public void onMessage(MessageReceivedEvent event) {
        String message = event.getMessage().getContentRaw();

        if (message.startsWith("$")) {
            dispatcher.execute(
                    new DiscordContext(event),
                    message.substring(1)
            );
        }
    }
}
