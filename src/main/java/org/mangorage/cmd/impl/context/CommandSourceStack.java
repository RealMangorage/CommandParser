package org.mangorage.cmd.impl.context;

import org.mangorage.cmd.Util;
import org.mangorage.cmd.api.IArgumentType;
import org.mangorage.cmd.api.ICommandSourceStack;
import org.mangorage.cmd.impl.argument.ParseError;
import org.mangorage.cmd.impl.argument.ParseResult;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;

public final class CommandSourceStack<S> implements ICommandSourceStack<S> {

    public static <S> CommandSourceStack<S> of(S context, String[] args) {
        return new CommandSourceStack<>(context, args);
    }

    private final Map<String, ParseError> parseErrors = new HashMap<>();
    private final S context;
    private final String[] args;

    private Map<String, IArgumentType<?>> parameters;
    private String[] remaining;
    private String[] previousRemaining;

    private CommandSourceStack(S context, String[] args) {
        this.context = context;
        this.args = args;
        this.remaining = args;
    }

    @Override
    public <O> O getParameter(String id, IArgumentType<O> parser) {
        var actualType = parameters.get(id);
        if (actualType == null)
            throw new IllegalStateException("Invalid Parameter Type %s".formatted(id));
        if (actualType != parser)
            throw new IllegalStateException("Expected Argument Type with Class of %s instead got %s".formatted(parser.getType(), actualType.getType()));

        ParseResult<O> result = parser.parse(remaining);
        if (result.getError() != null) {
            parseErrors.put(id, result.getError());
        }
        this.previousRemaining = this.remaining;
        this.remaining = result.getRemaining();
        return result.getResult();
    }

    @Override
    public <O> Optional<O> getOptionalParameter(String id, IArgumentType<O> parser) {
        var actualType = parameters.get(id);
        if (actualType == null)
            throw new IllegalStateException("Invalid Parameter Type %s".formatted(id));
        if (actualType != parser)
            throw new IllegalStateException("Expected Argument Type with Class of %s instead got %s".formatted(parser.getType(), actualType.getType()));

        try {
            ParseResult<O> result = parser.parse(remaining);
            if (result.getError() != null) {
                parseErrors.put(id, result.getError());
            }
            this.previousRemaining = this.remaining;
            this.remaining = result.getRemaining();
            return Optional.of(result.getResult());
        } catch (Throwable throwable) {
            return Optional.empty();
        }
    }

    @Override
    public Map<String, ParseError> getParsingErrors() {
        return Map.copyOf(parseErrors);
    }

    @Override
    public void ifErrorPresent(String id, Predicate<ParseError> predicate, Consumer<ParseError> consumer) {
        var error = parseErrors.get(id);
        if (error != null) {
            if (predicate.test(error))
                consumer.accept(error);
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
    public void updateParameters(Map<String, IArgumentType<?>> parameters) {
        this.parameters = Map.copyOf(parameters);
        this.parseErrors.clear();
    }
}
