package org.mangorage.cmd.core.argument;

public final class ParseResult<O> {
    private final O result;
    private final String[] remaining;

    public ParseResult(O result, String[] remaining) {
        this.result = result;
        this.remaining = remaining;
    }

    public O getResult() {
        return result;
    }

    public String[] getRemaining() {
        return remaining;
    }
}
