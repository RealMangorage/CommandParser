package org.mangorage.example.commands;

import org.mangorage.cmd.api.ICommand;
import org.mangorage.cmd.impl.argument.ArgumentTypes;
import org.mangorage.cmd.impl.argument.ParseError;
import org.mangorage.example.AutoRegister;
import org.mangorage.example.DiscordContext;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AutoRegister
public final class TrickCommand {

    private final Map<String, String> tricks = new HashMap<>();

    public ICommand<DiscordContext> create() {

        ICommand<DiscordContext> create = DiscordContext.literal("create")
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
                .withArguments(
                        List.of(
                                DiscordContext.argument("id", ArgumentTypes.STRING)
                                        .onError((s, e) -> {
                                            if (e == ParseError.MALFORMED) {
                                                s.getContext().reply("Error with parameter 'id', Malformed Input, Invalid for parameter type String");
                                            } else if (e == ParseError.INCOMPLETE) {
                                                s.getContext().reply("Error with parameter 'id', Either Incomplete or Missing");
                                            }
                                        })
                                        .build(),
                                DiscordContext.argument("content", ArgumentTypes.STRING_ALL)
                                        .onError((s, e) -> {
                                            s.getContext().reply("Error with Parameter 'content', %s".formatted(e));
                                        })
                                        .build()
                        )
                )
                .build();

        ICommand<DiscordContext> show = DiscordContext.literal("show")
                .executes(s -> {
                    var context = s.getContext();
                    var id = s.getParameter("id", ArgumentTypes.STRING);
                    var content = tricks.getOrDefault(id, "Trick with ID %s not found.".formatted(id));

                    context.reply(content);

                    return 1;
                })
                .withArgument(
                        DiscordContext.argument("id", ArgumentTypes.STRING)
                                .onError((s, e) -> {
                                    s.getContext().reply("Error with Parameter 'content', %s".formatted(e));
                                })
                                .build()
                )
                .build();

        return DiscordContext.literal("trick")
                .executes(s -> {
                    s.getContext().reply("trick create/show <id> <create:content>");
                    return 1;
                })
                .subCommands(
                        List.of(
                            create,
                            show
                        )
                )
                .build();
    }
}
