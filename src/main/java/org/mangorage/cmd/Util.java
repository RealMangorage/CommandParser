package org.mangorage.cmd;

import java.util.Arrays;

public class Util {
    public static String[] popArray(String[] array) {
        if (array.length <= 1) {
            return new String[]{};
        } else {
            return Arrays.copyOfRange(array, 1, array.length);
        }
    }
}
