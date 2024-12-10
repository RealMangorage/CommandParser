package org.mangorage.testcl;

import org.mangorage.testcl.asmstuff.MathTest;
import org.mangorage.testcl.asmstuff.Test;

public class TestingCL {
    public TestingCL() throws ClassNotFoundException {

        Class.forName(Object.class.getName(), false, Thread.currentThread().getContextClassLoader());

        System.out.println(
                new Test()
                        .create("COOL", "ALR", "WHAT")
        );

        System.out.println(
                new MathTest()
                        .calculate(1, 209)
        );

    }
}
