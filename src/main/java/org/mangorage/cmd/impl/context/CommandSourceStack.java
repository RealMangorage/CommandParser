package org.mangorage.cmd.impl.context;

import org.mangorage.cmd.Util;
import org.mangorage.cmd.api.IArgumentType;
import org.mangorage.cmd.api.ICommandSourceStack;
import org.mangorage.cmd.impl.argument.ParseResult;

import java.util.Map;
import java.util.Optional;

public final class CommandSourceStack<S> implements ICommandSourceStack<S> {

    public static <S> CommandSourceStack<S> of(S context, String[] args) {
        return new CommandSourceStack<>(context, args);
    }

    private final S context;
    private final String[] args;

    private Map<String, IArgumentType<?>> parameters;
    private String[] remaining;

    private CommandSourceStack(S context, String[] args) {
        this.context = context;
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

    @Override
    public S getContext() {
        return context;
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

    @Override
    public String[] getRemainingArgs() {
        return remaining;
    }

    @Override
    public void shrinkArgs() {
        this.remaining = Util.shrinkArray(getRemainingArgs());
    }

    @Override
    public void updateParameters(Map<String, IArgumentType<?>> parameters) {
        this.parameters = Map.copyOf(parameters);
    }
}
