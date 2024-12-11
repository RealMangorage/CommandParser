package org.mangorage.testcl;

import org.mangorage.classloader.CustomizedClassloader;
import org.mangorage.classloader.features.transformers.ITransformer;
import org.mangorage.classloader.features.transformers.impl.InterfaceTransformer;

import java.lang.constant.ClassDesc;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public class ClassloaderTest {
    public static void main(String[] args) throws InterruptedException {
        var cl = CustomizedClassloader.of(Thread.currentThread().getContextClassLoader().getParent())
                .withJavaClasspath()
                .addClassGenerator(new ClassGen())
                .addTransformer(
                        new InterfaceTransformer(
                                TestInvoker.class.describeConstable().orElseThrow(),
                                ClassDesc.of("org.mangorage.testOk")
                        )
                )
                .buildWithBootstrap();

        Function<Object[], Class<?>> function = (o) -> {
            try {
                return cl.tryGenerateAndTransformClass(
                        (List<ITransformer>) o[0],
                        (String) o[1],
                        (byte[]) o[2]
                );
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        };

        Supplier<List<ITransformer>> l = cl::getTransformers;

        try {
            Class.forName(
                    "org.mangorage.testcl.TestingCL",
                    false,
                    cl
            ).getConstructor(Function.class, Supplier.class).newInstance(function, l);
        } catch (Throwable e) {
            e.printStackTrace();
        }

        Thread.sleep(10000);

    }
}