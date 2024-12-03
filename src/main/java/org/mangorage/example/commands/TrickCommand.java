package org.mangorage.example.commands;

import org.mangorage.cmd.api.ICommand;
import org.mangorage.cmd.impl.Command;
import org.mangorage.cmd.impl.CommandArgument;
import org.mangorage.cmd.impl.argument.ArgumentTypes;
import org.mangorage.cmd.impl.argument.ParseError;
import org.mangorage.example.DiscordContext;
import org.mangorage.example.api.ICommandRegistrar;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class TrickCommand implements ICommandRegistrar<ICommand<DiscordContext>> {

    private final Map<String, String> tricks = new HashMap<>();

    @Override
    public ICommand<DiscordContext> create() {

        ICommand<DiscordContext> create = Command.literal("create", DiscordContext.class)
                .onError(s -> {
                    s.ifErrorPresent("content", e -> e == ParseError.INCOMPLETE, s2 -> {
                        s.getContext().reply("Content Parameter Incomplete");
                    });
                })
                .executes(s -> {
                    var context = s.getContext();

                    var id = s.getParameter("id", ArgumentTypes.STRING);
                    var content = s.getParameter("content", ArgumentTypes.STRING_ALL);

                    if (tricks.containsKey(id)) {
                        context.reply("Already have trick with id %s".formatted(id));
                        return 1;
                    } else {
                        if (content.isEmpty() || content.isBlank()) {
                            context.reply("Content empty");
                            return 1;
                        }
                        tricks.put(id, content);
                        context.reply("Created new Trick with id %s".formatted(id));
                        return 1;
                    }
                })
                .withArgument(
                        CommandArgument.literal("id", ArgumentTypes.STRING, DiscordContext.class)
                                .onError((s, e) -> {
                                    s.getContext().reply("Error with Parameter 'content', %s".formatted(e));
                                })
                                .build()
                )
                .withArgument(
                        CommandArgument.literal("content", ArgumentTypes.STRING_ALL, DiscordContext.class)
                                .onError((s, e) -> {
                                    s.getContext().reply("Error with Parameter 'content', %s".formatted(e));
                                })
                                .build()
                )
                .build();

        ICommand<DiscordContext> show = Command.literal("show", DiscordContext.class)
                .executes(s -> {
                    var context = s.getContext();
                    var id = s.getParameter("id", ArgumentTypes.STRING);
                    var content = tricks.getOrDefault(id, "Trick with ID %s not found.".formatted(id));

                    context.reply(content);

                    return 1;
                })
                .withArgument(
                        CommandArgument.literal("id", ArgumentTypes.STRING, DiscordContext.class)
                                .onError((s, e) -> {
                                    s.getContext().reply("Error with Parameter 'content', %s".formatted(e));
                                })
                                .build()
                )
                .build();

        return Command.literal("trick", DiscordContext.class)
                .executes(s -> {
                    s.getContext().reply("trick create/show <id> <create:content>");
                    return 1;
                })
                .subCommands(
                        List.of(create, show)
                )
                .build();
    }
}
