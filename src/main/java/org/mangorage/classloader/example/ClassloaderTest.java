package org.mangorage.classloader.example;

import org.mangorage.classloader.CustomizedClassloader;

import java.nio.file.Path;

public class ClassloaderTest {
    public static void main(String[] args) {
        ClassLoader parent = Thread.currentThread().getContextClassLoader().getParent();
        CustomizedClassloader cl = CustomizedClassloader.of(parent)
                .addTransformer(new TestTransformer())
                .addUrl(
                        Path.of("F:\\Minecraft Forge Projects\\CMDParser\\Example\\build\\libs\\Example-1.0-SNAPSHOT.jar")
                )
                .build();
        try {
            Thread.currentThread().setContextClassLoader(cl);
            Class.forName("org.mangorage.testcl.TestingCL", false, cl).newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
