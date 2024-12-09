package org.mangorage.classloader;

import org.mangorage.classloader.example.MathTestTransformer;
import org.mangorage.classloader.example.TestTransformer;
import org.mangorage.classloader.misc.CustomizedClassloader;
import org.mangorage.classloader.transform.finders.DefaultTransformerFinder;

import java.nio.file.Path;

public class ClassloaderTest {
    public static void main(String[] args) throws InterruptedException {
        ClassLoader parent = Thread.currentThread().getContextClassLoader().getParent();
        CustomizedClassloader cl = CustomizedClassloader.of(parent)
                .addTransformer(new TestTransformer())
                .addTransformer(new MathTestTransformer())
                .setTransformerLocator(new DefaultTransformerFinder("services/default.transformers"))
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