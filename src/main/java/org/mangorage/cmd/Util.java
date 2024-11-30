package org.mangorage.cmd;

import org.mangorage.cmd.core.Command;
import org.mangorage.cmd.core.CommandRegistry;
import org.mangorage.cmd.core.context.CommandResult;

import java.util.Arrays;

public class Util {
    public static String[] shrinkArray(String[] array) {
        if (array.length <= 1) {
            return new String[]{};
        } else {
            return Arrays.copyOfRange(array, 1, array.length);
        }
    }

    public static Command.Builder<CommandRegistry.GlobalContext, CommandResult> literal() {
        return Command.literal(CommandRegistry.GlobalContext.class, CommandResult.class);
    }
}
