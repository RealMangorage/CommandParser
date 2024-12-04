package org.mangorage.command.impl.argument;

import org.mangorage.command.impl.misc.Util;
import org.mangorage.command.api.IArgumentType;

public final class ArgumentTypes {
    public static final IArgumentType<Integer> INT = new ArgumentTypeImpl<>(Integer.class, p -> new ParseResult<>(
            Integer.parseInt(p[0]),
            Util.shrinkArray(p)
    ));

    public static final IArgumentType<Boolean> BOOL = new ArgumentTypeImpl<>(Boolean.class, p -> new ParseResult<>(
            Boolean.parseBoolean(p[0]),
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
