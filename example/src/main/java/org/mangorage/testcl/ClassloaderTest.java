package org.mangorage.testcl;

import org.mangorage.classloader.CustomizedClassloader;
import org.mangorage.classloader.features.transformers.impl.MethodHandleTransformer;

public class ClassloaderTest {
    public static void main(String[] args) throws InterruptedException {



        var cl = CustomizedClassloader.of(Thread.currentThread().getContextClassLoader().getParent())
                .withJavaClasspath()
                .addClassGenerator(new ClassGen())
                .addTransformer(new MethodHandleTransformer())
                .buildWithBootstrap();

        try {

            Class.forName(
                    "org.mangorage.testcl.TestingCL",
                    false,
                    cl
            ).newInstance();
        } catch (Throwable e) {
            e.printStackTrace();
        }

        Thread.sleep(10000);

    }
}