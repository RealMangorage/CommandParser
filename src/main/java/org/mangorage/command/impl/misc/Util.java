package org.mangorage.command.impl.misc;

import java.util.Arrays;

public class Util {
    public static String[] shrinkArray(String[] array) {
        return array.length <= 1 ? new String[] {} : Arrays.copyOfRange(array, 1, array.length);
    }
}
