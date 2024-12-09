package org.mangorage.command.example.args;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.User;

public class UserArgumentType implements ArgumentType<User> {

    public static UserArgumentType globalUser(JDA jda) {
        return new UserArgumentType(jda);
    }

    private final JDA jda;

    private UserArgumentType(JDA jda) {
        this.jda = jda;
    }

    @Override
    public User parse(StringReader reader) throws CommandSyntaxException {
        final int start = reader.getCursor();
        final long result = reader.readLong();
        final User user = jda.retrieveUserById(result).complete();
        if (user == null) {
            reader.setCursor(start);
            throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherUnknownArgument().createWithContext(reader);
        }
        return user;
    }

}
