package org.mangorage.command.impl.misc;

import java.util.function.Predicate;

public final class Validators {
    public static Predicate<Integer> intRange(int min, int max) {
        return i -> i >= min && i <= max;
    }
}
