package org.mangorage.cmd.core.context;

import org.mangorage.cmd.core.IParser;

public final class Context {

    public static Context of(String[] args) {
        return new Context(args);
    }

    private final String[] args;

    private Context(String[] args) {
        this.args = args;
    }

    public <O> O getObject(IParser<O> parser) {
        return parser.parse(args);
    }

    public String[] getArgs() {
        return args;
    }
}
