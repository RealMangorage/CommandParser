package org.mangorage.cmd.impl.argument;

import org.mangorage.cmd.Util;
import org.mangorage.cmd.api.IArgumentType;

public final class ArgumentTypes {
    public static final IArgumentType<Integer> INT = new ArgumentTypeImpl<>(Integer.class, p -> {
        if (p.length == 0)
            return new ParseResult<>(ParseError.INCOMPLETE);

        return new ParseResult<>(
                Integer.parseInt(p[0]),
                Util.shrinkArray(p)
        );
    });
}
