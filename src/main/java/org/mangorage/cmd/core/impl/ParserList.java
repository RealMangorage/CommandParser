package org.mangorage.cmd.core.impl;

import org.mangorage.cmd.core.IParser;

public final class ParserList {
    public static final IParser<Integer> INT = p -> Integer.parseInt(p[0]);
}
