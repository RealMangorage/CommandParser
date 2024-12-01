package org.mangorage.cmd.impl.argument;

import org.jetbrains.annotations.Nullable;

public final class ParseResult<O> {
    private final O result;
    private final String[] remaining;
    private ParseError error;

    public ParseResult(ParseError error, String[] args) {
        this.error = error;
        this.result = null;
        this.remaining = null;
    }

    public ParseResult(O result, String[] remaining) {
        this.result = result;
        this.remaining = remaining;
    }

    public O getResult() {
        return result;
    }

    public @Nullable ParseError getError() {
        return error;
    }

    public String[] getRemaining() {
        return remaining;
    }
}
