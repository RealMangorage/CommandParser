package org.mangorage.testcl;


public class TestingCL {
    public TestingCL() throws Throwable {
        if (Class.forName("org.mangorage.testOk").newInstance() instanceof TestInvoker invoker) {
            System.out.println(invoker.test(2));
        }
    }
}
