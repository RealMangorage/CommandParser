package org.mangorage.testcl;

import org.mangorage.classloader.features.handle.MethodHandle;

public class TestingCL {
    public TestingCL() throws Throwable {
        MethodHandle<Integer> test = new MethodHandle<>(
                "",
                "",
                false,
                Integer.class
        ) {
            @Override
            public int hashCode() {
                return 12 + super.hashCode();
            }
        };
    }
}
