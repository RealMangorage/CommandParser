package org.mangorage.testcl;

import org.mangorage.classloader.features.transformers.ITransformer;

import java.lang.constant.ClassDesc;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public class TestingCL {
    public TestingCL(Function<Object[], Class<?>> generator, Supplier<List<ITransformer>> listSupplier) throws Throwable {
        if (Class.forName("org.mangorage.testOk").newInstance() instanceof TestInvoker invoker) {
            System.out.println(invoker.test(2));
        }

        var a = generator.apply(
                new Object[]{
                        listSupplier.get(),
                        "org.mangorage.TEUK",
                        ClassGen.getBytes(ClassDesc.of("org.mangorage.TEUK"))
                }
        );
        System.out.println(Class.forName("org.mangorage.TEUK"));
    }
}
