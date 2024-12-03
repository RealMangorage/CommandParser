package org.mangorage.cmd.impl.argument;

import org.mangorage.cmd.Util;
import org.mangorage.cmd.api.IArgumentType;

import java.util.Arrays;

public final class ArgumentTypes {
    public static final IArgumentType<Integer> INT = new ArgumentTypeImpl<>(Integer.class, p -> new ParseResult<>(
            Integer.parseInt(p[0]),
            Util.shrinkArray(p)
    ));

    public static final IArgumentType<String> STRING = new ArgumentTypeImpl<>(String.class, p -> new ParseResult<>(
            p[0],
            Util.shrinkArray(p)
    ));

    public static final IArgumentType<String> STRING_ALL = new ArgumentTypeImpl<>(String.class, p -> new ParseResult<>(
            String.join(" ", p),
            new String[]{}
    ));
}
