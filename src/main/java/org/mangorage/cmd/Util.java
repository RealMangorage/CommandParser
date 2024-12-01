package org.mangorage.cmd;

import org.mangorage.cmd.impl.Command;
import org.mangorage.cmd.impl.CommandDispatcher;

import java.util.Arrays;

public class Util {
    public static String[] shrinkArray(String[] array) {
        if (array.length <= 1) {
            return new String[]{};
        } else {
            return Arrays.copyOfRange(array, 1, array.length);
        }
    }

    public static String removeFirst(String string) {
        return string.substring(1);
    }

    public static Command.Builder<CommandDispatcher.GlobalContext> literal() {
        return Command.literal(CommandDispatcher.GlobalContext.class);
    }
}
