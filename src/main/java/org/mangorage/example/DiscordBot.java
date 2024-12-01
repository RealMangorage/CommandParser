package org.mangorage.example;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.hooks.AnnotatedEventManager;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
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

    private int timer = 0;

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
                    var seconds = s.getParameter("seconds", ArgumentTypes.INT);
                    timer += seconds;
                    context.reply(
                            context.getUser().getGlobalName() + " Added " + seconds + " seconds to the timer!"
                    );
                    return 1;
                })
                .withParameter("seconds", ArgumentTypes.INT)
                .build();

        ICommand<DiscordContext> remove = Command.literal(DiscordContext.class)
                .executes(s -> {
                    var context = s.getContext();
                    var seconds = s.getParameter("seconds", ArgumentTypes.INT);
                    timer -= seconds;
                    context.reply(
                            context.getUser().getGlobalName() + " Removed " + seconds + " seconds from the timer!"
                    );
                    return 1;
                })
                .withParameter("seconds", ArgumentTypes.INT)
                .build();

        ICommand<DiscordContext> set = Command.literal(DiscordContext.class)
                .executes(s -> {
                    var context = s.getContext();
                    var seconds = s.getParameter("seconds", ArgumentTypes.INT);
                    timer = seconds;
                    context.reply(
                            context.getUser().getGlobalName() + " Set timer to " + seconds + " seconds!"
                    );
                    return 1;
                })
                .withParameter("seconds", ArgumentTypes.INT)
                .build();

        ICommand<DiscordContext> info = Command.literal(DiscordContext.class)
                .executes(s -> {
                    var context = s.getContext();
                    context.reply(
                            "Timer has " + timer + " seconds!"
                    );
                    return 1;
                })
                .build();

        ICommand<DiscordContext> time = Command.literal(DiscordContext.class)
                .executes(s -> {
                    s.getContext().reply(
                            """
                            Usage:
                                time add/remove/set <seconds:int>
                                time info
                            """
                    );
                    return 1;
                })
                .subCommand("add", add)
                .subCommand("remove", remove)
                .subCommand("set", set)
                .subCommand("info", info)
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
