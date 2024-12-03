package org.mangorage.cmd.impl.argument;

import org.mangorage.cmd.api.IArgumentType;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

public final class ArgumentTypeImpl<O> implements IArgumentType<O> {
    private final Class<O> type;
    private final Function<String[], ParseResult<O>> function;

    public ArgumentTypeImpl(Class<O> oClass, Function<String[], ParseResult<O>> function) {
        this.type = oClass;
        this.function = function;
    }

    @Override
    public ParseResult<O> parse(Predicate<O> validator, String[] args) {
        // Do it automatically, we cant parse if null/empty anyway?
        if (args.length == 0)
            return new ParseResult<>(ParseError.INCOMPLETE);

        try {
            var result = function.apply(args);
            if (validator.test(result.getResult())) {
                return result;
            } else {
                return new ParseResult<>(ParseError.INVALID);
            }
        } catch (Throwable throwable) {
            return new ParseResult<>(ParseError.MALFORMED);
        }
    }

    @Override
    public Class<O> getArgumentClass() {
        return type;
    }
}
