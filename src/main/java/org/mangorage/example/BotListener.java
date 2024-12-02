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
            int result = dispatcher.execute(
                    new DiscordContext(event),
                    message.substring(1)
            );
            if (result == 0)
                event.getMessage().reply("No Permission!").queue();
            if (result == 2)
                event.getMessage().reply("Error Occured").queue();
        }
    }
}
