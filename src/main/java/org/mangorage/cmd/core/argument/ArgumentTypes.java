package org.mangorage.cmd.core.argument;

import org.mangorage.cmd.Util;

public final class ArgumentTypes {
    public static final IArgumentType<Integer> INT = new ArgumentTypeImpl<>(Integer.class, p -> {
        return new ParseResult<>(
                Integer.parseInt(p[0]),
                Util.shrinkArray(p)
        );
    });
}
