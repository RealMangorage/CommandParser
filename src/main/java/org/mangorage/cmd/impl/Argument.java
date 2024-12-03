package org.mangorage.cmd.impl;

import org.mangorage.cmd.api.IArgument;
import org.mangorage.cmd.api.ICommandSourceStack;

public final class Argument<S> implements IArgument<S> {

    @Override
    public int execute(ICommandSourceStack<S> commandSourceStack) {
        return 0;
    }

    @Override
    public String getId() {
        return "";
    }
}
