package org.mangorage.cmd.core.context;

import org.mangorage.cmd.core.IArgumentType;

import java.util.Map;

public final class Context {

    public static Context of(Map<String, IArgumentType<?>> parameters, String[] args) {
        return new Context(parameters, args);
    }

    private final Map<String, IArgumentType<?>> parameters;
    private final String[] args;

    private Context(Map<String, IArgumentType<?>> parameters, String[] args) {
        this.parameters = parameters;
        this.args = args;
    }

    public <O> O getParameter(String id, IArgumentType<O> parser) {
        var actualType = parameters.get(id);
        if (actualType == null)
            throw new IllegalStateException("Invalid Parameter Type %s".formatted(id));
        if (actualType != parser)
            throw new IllegalStateException("Expected Argument Type with Class of %s instead got %s".formatted(parser.getType(), actualType.getType()));

        return parser.parse(args);
    }

    public String[] getArgs() {
        return args;
    }
}
