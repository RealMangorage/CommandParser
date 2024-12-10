package org.mangorage.testcl;

import org.mangorage.testcl.asmstuff.MathTest;
import org.mangorage.testcl.asmstuff.Test;

public class TestingCL {
    public static void main(String[] args) throws Throwable {
        new TestingCL();
    }

    public TestingCL() throws Throwable {
        System.out.println("OK LOADED TESTINGG CL");
        System.out.println(
                new Test()
                        .create("COOL", "ALR", "WHAT")
        );

        System.out.println(
                new MathTest()
                        .calculate(210, 10)
        );
    }
}
