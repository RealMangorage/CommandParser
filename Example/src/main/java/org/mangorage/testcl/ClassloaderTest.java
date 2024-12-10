package org.mangorage.testcl;


import org.mangorage.classloader.CustomizedClassloader;
import org.mangorage.classloader.features.locators.DefaultTransformerLocator;

import java.util.Arrays;
import java.util.stream.Collectors;

public class ClassloaderTest {
    public static void main(String[] args) throws InterruptedException, InstantiationException, IllegalAccessException {
        System.out.println("Detected Class Path");
        System.out.println("---------------------------------------------------------");
        System.out.println(
                Arrays.stream(System.getProperty("java.class.path").split(";"))
                        .collect(Collectors.joining("\n"))
        );
        System.out.println("---------------------------------------------------------");

        ClassLoader parent = Thread.currentThread().getContextClassLoader().getParent();
        CustomizedClassloader cl = CustomizedClassloader.of(parent)
                .withJavaClasspath()
                .addClassGenerator(new ClassGen())
                .setTransformerLocator(new DefaultTransformerLocator("services/default.transformers"))
                .build();


        try {
            Thread.currentThread().setContextClassLoader(cl);
            Class.forName("org.mangorage.testcl.TestingCL", false, cl).newInstance();
        } catch (Throwable e) {
            e.printStackTrace();
        }

        Thread.sleep(10000);

        cl.getTransformedInfo("org.mangorage.testcl.Test").forEach(result -> {
            System.out.println(result.name());
        });
    }
}