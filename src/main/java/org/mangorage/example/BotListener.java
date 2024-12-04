package org.mangorage.example;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.StatusChangeEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.SubscribeEvent;
import java.util.EnumMap;
import java.util.List;

public final class BotListener {
    private final CommandDispatcher<DiscordContext> dispatcher;
    private final EnumMap<JDA.Status, String> map = new EnumMap<>(JDA.Status.class);


    private void register(JDA.Status status, String msg) {
        map.put(status, msg);
    }

    public BotListener(CommandDispatcher<DiscordContext> dispatcher) {
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
            DiscordContext context = new DiscordContext(event);
            ParseResults<DiscordContext> parsed = dispatcher.parse(message.substring(1), context);
            try {
                int result = dispatcher.execute(parsed);
            } catch (CommandSyntaxException e) {
                var node =  dispatcher.findNode(
                        List.of(
                                message.substring(1)
                        )
                );
                if (node != null) {
                    String[] usage = dispatcher.getAllUsage(
                            node,
                            context,
                            true
                    );
                    event.getMessage().reply(
                            """
                            Usage for '%s':
                            %s
                            """.formatted(
                                    node.getName(),
                                    String.join("\n", usage)
                            )
                    ).queue();
                } else {
                    event.getMessage().reply(e.getMessage()).queue();
                }
            }
        }
    }

    @SubscribeEvent
    public void onStatus(StatusChangeEvent event) {
        System.out.println(
                map.get(event.getNewStatus())
        );
    }
}
