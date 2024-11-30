package org.mangorage.cmd.core.impl;

import org.mangorage.cmd.core.IArgumentType;

public final class ArgumentTypes {
    public static final IArgumentType<Integer> INT = new ArgumentTypeImpl<>(Integer.class, p -> Integer.parseInt(p[0]));
}
