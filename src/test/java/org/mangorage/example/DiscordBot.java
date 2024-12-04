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
import org.mangorage.cmd.impl.CommandDispatcher;
import org.mangorage.example.commands.TimeCommand;
import org.mangorage.example.commands.TrickCommand;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

public final class DiscordBot {
    public static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    public static void main(String[] args) throws FileNotFoundException {
        new DiscordBot();
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

        dispatcher.autoRegister(AutoRegister.class, c -> {
            System.out.println("Callback " + c);
        });

        JDA.addEventListener(new BotListener(dispatcher));
    }
}
