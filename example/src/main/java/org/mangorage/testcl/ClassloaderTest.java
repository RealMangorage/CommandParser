package org.mangorage.testcl;

import org.mangorage.classloader.CustomizedClassloader;
import org.mangorage.classloader.example.transformers.MathTestTransformer;
import org.mangorage.classloader.example.transformers.TestTransformer;

public class ClassloaderTest {
    public static void main(String[] args) throws InterruptedException {



        var cl = CustomizedClassloader.of(Thread.currentThread().getContextClassLoader().getParent())
                .withJavaClasspath()
                .addTransformer(new TestTransformer())
                .addTransformer(new MathTestTransformer())
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