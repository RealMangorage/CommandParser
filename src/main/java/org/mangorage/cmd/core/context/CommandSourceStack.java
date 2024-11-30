package org.mangorage.cmd.core.context;

import org.mangorage.cmd.core.argument.IArgumentType;
import org.mangorage.cmd.core.argument.ParseResult;

import java.util.Map;
import java.util.Optional;

public final class CommandSourceStack {

    public static CommandSourceStack of(Map<String, IArgumentType<?>> parameters, String[] args) {
        return new CommandSourceStack(parameters, args);
    }

    private final Map<String, IArgumentType<?>> parameters;
    private final String[] args;
    private String[] remaining;

    private CommandSourceStack(Map<String, IArgumentType<?>> parameters, String[] args) {
        this.parameters = parameters;
        this.args = args;
        this.remaining = args;
    }

    public <O> O getParameter(String id, IArgumentType<O> parser) {
        var actualType = parameters.get(id);
        if (actualType == null)
            throw new IllegalStateException("Invalid Parameter Type %s".formatted(id));
        if (actualType != parser)
            throw new IllegalStateException("Expected Argument Type with Class of %s instead got %s".formatted(parser.getType(), actualType.getType()));

        ParseResult<O> result = parser.parse(remaining);
        this.remaining = result.getRemaining();
        return result.getResult();
    }

    public <O> Optional<O> getOptionalParameter(String id, IArgumentType<O> parser) {
        try {
            return Optional.of(getParameter(id, parser));
        } catch (Throwable e) {
            return Optional.empty();
        }
    }

    public String[] getArgs() {
        return args;
    }

    public String[] getRemaining() {
        return remaining;
    }
}
