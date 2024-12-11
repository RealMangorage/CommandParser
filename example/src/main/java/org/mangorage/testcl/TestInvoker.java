package org.mangorage.testcl;

public interface TestInvoker<G> {
    default int test(int a) {
        throw new IllegalStateException("Not overridden");
    }
}
