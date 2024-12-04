package org.mangorage.command.impl.context;

import org.mangorage.command.impl.misc.Util;
import org.mangorage.command.api.IArgument;
import org.mangorage.command.api.IArgumentType;
import org.mangorage.command.api.ICommandSourceStack;
import org.mangorage.command.impl.argument.ParseResult;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

public final class CommandSourceStack<S> implements ICommandSourceStack<S> {

    public static <S> CommandSourceStack<S> of(S context, String[] args) {
        return new CommandSourceStack<>(context, args);
    }

    private final S context;
    private final String[] args;

    private Map<String, IArgument<S>> parameters;
    private String[] remaining;
    private String[] previousRemaining;

    private CommandSourceStack(S context, String[] args) {
        this.context = context;
        this.args = args;
        this.remaining = args;
    }

    @SuppressWarnings("all")
    @Override
    public <O> O getParameter(String id, IArgumentType<O> type) {
        var actualType = parameters.get(id);
        if (actualType == null)
            throw new IllegalStateException("Invalid Parameter Type %s".formatted(id));
        if (actualType.getType() != type)
            throw new IllegalStateException("Expected Argument Type with Class of %s instead got %s".formatted(type.getArgumentClass(), actualType.getType().getArgumentClass()));

        ParseResult<O> result = type.parse((Predicate<O>) actualType.getPredicate(), remaining);

        if (result.getError() != null) {
            actualType.getErrorConsumer().accept(this, result.getError());
        }

        this.previousRemaining = this.remaining;
        this.remaining = result.getRemaining();
        return result.getResult();
    }

    @SuppressWarnings("all")
    @Override
    public <O> Optional<O> getOptionalParameter(String id, IArgumentType<O> type) {
        var actualType = parameters.get(id);
        if (actualType == null)
            throw new IllegalStateException("Invalid Parameter Type %s".formatted(id));
        if (actualType.getType() != type)
            throw new IllegalStateException("Expected Argument Type with Class of %s instead got %s".formatted(type.getArgumentClass(), actualType.getType()));

        try {
            ParseResult<O> result = type.parse((Predicate<O>) actualType.getPredicate(), remaining);

            // TODO: Figure out how to handle when a Optional Parameter errors?

            this.previousRemaining = this.remaining;
            this.remaining = result.getRemaining();
            return Optional.of(result.getResult());
        } catch (Throwable throwable) {
            return Optional.empty();
        }
    }

    @Override
    public S getContext() {
        return context;
    }

    public String[] getArgs() {
        return args;
    }

    @Override
    public String[] getRemainingArgs() {
        return remaining;
    }

    @Override
    public String[] getPreviousRemainingArgs() {
        return previousRemaining;
    }

    @Override
    public void shrinkArgs() {
        this.remaining = Util.shrinkArray(getRemainingArgs());
    }

    @Override
    public void updateParameters(Map<String, IArgument<S>> parameters) {
        this.parameters = Map.copyOf(parameters);
    }
}
