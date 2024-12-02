package org.mangorage.example;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.StatusChangeEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.SubscribeEvent;
import org.mangorage.cmd.api.ICommandDispatcher;

import java.util.EnumMap;

public final class BotListener {
    private final ICommandDispatcher<DiscordContext> dispatcher;
    private final EnumMap<JDA.Status, String> map = new EnumMap<>(JDA.Status.class);


    private void register(JDA.Status status, String msg) {
        map.put(status, msg);
    }

    public BotListener(ICommandDispatcher<DiscordContext> dispatcher) {
        this.dispatcher = dispatcher;
        for (JDA.Status value : JDA.Status.values()) {
            register(
                    value,
                    "Bot " + value.toString().toLowerCase()
            );
        }
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
        }
    }

    @SubscribeEvent
    public void onStatus(StatusChangeEvent event) {
        System.out.println(
                map.get(event.getNewStatus())
        );
    }
}
