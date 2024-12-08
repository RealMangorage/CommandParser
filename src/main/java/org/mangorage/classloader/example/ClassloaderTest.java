package org.mangorage.classloader.example;

import org.mangorage.classloader.CustomizedClassloader;

import java.nio.file.Path;

public class ClassloaderTest {
    public static void main(String[] args) throws InterruptedException {
        ClassLoader parent = Thread.currentThread().getContextClassLoader().getParent();
        CustomizedClassloader cl = CustomizedClassloader.of(parent)
                .addTransformer(new TestTransformer())
                .addUrl(
                        Path.of("F:\\Minecraft Forge Projects\\CMDParser\\Example\\build\\libs\\Example-1.0-SNAPSHOT.jar")
                )
                .buildAndThenActivate("org.mangorage.testcl.TestingCL");

        Thread.sleep(10000);
        cl.getTransformedInfo("org.mangorage.testcl.Test").forEach(result -> {
            System.out.println(result.name());
        });
    }
}
