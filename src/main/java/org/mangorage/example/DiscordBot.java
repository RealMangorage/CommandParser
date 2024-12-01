package org.mangorage.example;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.hooks.AnnotatedEventManager;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.mangorage.cmd.Util;
import org.mangorage.cmd.api.ICommand;
import org.mangorage.cmd.api.ICommandDispatcher;
import org.mangorage.cmd.impl.Command;
import org.mangorage.cmd.impl.CommandDispatcher;
import org.mangorage.cmd.impl.argument.ArgumentTypes;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.file.Path;
import java.util.List;

public final class DiscordBot extends Thread {
    public static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    public static void main(String[] args) throws FileNotFoundException {
        new DiscordBot().start();
    }


    private final JDA JDA;
    private final ICommandDispatcher<DiscordContext> dispatcher = CommandDispatcher.create(DiscordContext.class);

    public DiscordBot() throws FileNotFoundException {
        this.JDA = JDABuilder.createDefault(
                GSON.fromJson(
                        new FileReader(
                                Path.of(".important/bot_token.json").toFile()
                        ),
                        Token.class
                ).token())
                .enableIntents(
                        List.of(
                                GatewayIntent.GUILD_MEMBERS,
                                GatewayIntent.MESSAGE_CONTENT
                        )
                )
                .enableCache(
                        List.of(
                                CacheFlag.VOICE_STATE
                        )
                )
                .setMemberCachePolicy(MemberCachePolicy.ALL)
                .setEventManager(new AnnotatedEventManager())
                .build();

        ICommand<DiscordContext> add = Command.literal(DiscordContext.class)
                .executes(s -> {
                    var context = s.getContext();
                    context.reply(
                            context.getUser().getGlobalName() + " Added " + s.getParameter("seconds", ArgumentTypes.INT) + " seconds to the timer!"
                    );
                })
                .withParameter("seconds", ArgumentTypes.INT)
                .build();

        ICommand<DiscordContext> time = Command.literal(DiscordContext.class)
                .executes(s -> {
                    s.getContext().reply(
                            "Usage: time set <seconds:int>"
                    );
                })
                .subCommand("add", add)
                .build();

        dispatcher.register("time", time);

        JDA.addEventListener(new BotListener(dispatcher));
    }

    @Override
    public void run() {
        try {
            JDA.awaitStatus(
                    net.dv8tion.jda.api.JDA.Status.CONNECTED
            );
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Bot Connected");
    }
}
