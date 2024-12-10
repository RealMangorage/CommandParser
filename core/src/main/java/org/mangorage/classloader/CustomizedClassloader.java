package org.mangorage.classloader;



import org.mangorage.classloader.features.generators.IClassGenerator;
import org.mangorage.classloader.features.locators.EmptyTransformerLocator;
import org.mangorage.classloader.features.locators.JavaTransformerLocator;
import org.mangorage.classloader.features.transformers.ITransformer;
import org.mangorage.classloader.features.locators.ITransformerLocator;
import org.mangorage.classloader.features.transformers.TransformResult;
import org.mangorage.classloader.features.transformers.TransformStack;
import org.mangorage.classloader.features.transformers.TransformedClass;

import java.io.IOException;
import java.io.InputStream;
import java.lang.classfile.ClassFile;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("preview")
public class CustomizedClassloader extends URLClassLoader {
    static {
        ClassLoader.registerAsParallelCapable();
    }

    public static Builder of(ClassLoader parent) {
        return new Builder(parent);
    }

    public static Builder of() {
        return new Builder(null);
    }


    private final ClassFile classFile = ClassFile.of();

    private final Map<String, Class<?>> classMap = new HashMap<>();
    private final Map<String, TransformedClass> transformedClassMap = new HashMap<>();
    private final ITransformerLocator finder;

    public CustomizedClassloader(URL[] urls, ClassLoader parent, ITransformerLocator finder, List<IClassGenerator> classGenerators) {
        super(urls, parent);
        this.finder = finder;
        classGenerators.forEach(cg -> {
            var unbaked = cg.generate(classFile);
            var clz = defineClass(unbaked.name(), unbaked.bytes());
            System.out.println("Successfully Generated " + clz);
        });
    }

    public List<TransformResult> getTransformedInfo(String name) {
        return transformedClassMap.containsKey(name) ? transformedClassMap.get(name).results() : List.of();
    }

    public byte[] getClassBytes(String className) throws IOException {
        InputStream is = getResourceAsStream(className.replace('.', '/') + ".class");
        byte[] buffer = new byte[is.available()];
        is.read(buffer);
        is.close();
        return buffer;
    }

    private Class<?> findAndStoreClass(String name) throws ClassNotFoundException {
        if (classMap.containsKey(name))
            return classMap.get(name);
        Class<?> clazz = super.findClass(name);
        classMap.put(name, clazz);
        return clazz;
    }

    @Override
    public Class<?> findClass(String name) throws ClassNotFoundException {
        System.out.println(name + " -> Customized");
        if (finder.findAndCacheTransformers().isEmpty())
            return findAndStoreClass(name);
        if (transformedClassMap.containsKey(name))
            return transformedClassMap.get(name).modifiedClass();

        try {
            List<ITransformer> transformersRemain = finder.findAndCacheTransformers().stream()
                    .filter(t -> t.handlesClass(name))
                    .toList();

            if (transformersRemain.isEmpty())
                return findAndStoreClass(name);

            byte[] original = getClassBytes(name);
            if (original == null)
                throw new IllegalStateException("Class Bytes were null for class " + name);

            TransformStack stack = TransformStack.of(original);
            transformersRemain.forEach(t -> t.transform(classFile, stack));

            Class<?> clazz = defineClass(name, stack.getModifiedOrOriginal());
            transformedClassMap.put(name, new TransformedClass(clazz, stack.getHistory()));
            return clazz;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    private Class<?> defineClass(String name, byte[] bytes) {
        return super.defineClass(name, bytes, 0, bytes.length);
    }


    public static class Builder {
        private final List<ITransformer> transformers = new ArrayList<>();
        private final List<IClassGenerator> generators = new ArrayList<>();

        private final List<URL> urls = new ArrayList<>();
        private final ClassLoader parent;
        private ITransformerLocator transformerFinder = EmptyTransformerLocator.INSTANCE;

        private Builder(ClassLoader parent) {
            this.parent = parent;
        }

        public Builder addTransformer(ITransformer transformer) {
            this.transformers.add(transformer);
            if (this.transformerFinder == EmptyTransformerLocator.INSTANCE)
                setTransformerLocator(new JavaTransformerLocator());
            return this;
        }

        public Builder addClassGenerator(IClassGenerator generator) {
            this.generators.add(generator);
            return this;
        }

        public Builder addClassGenerators(List<IClassGenerator> generator) {
            this.generators.addAll(generator);
            return this;
        }

        public Builder setTransformerLocator(ITransformerLocator finder) {
            this.transformerFinder = finder == null ? EmptyTransformerLocator.INSTANCE : finder;
            return this;
        }

        public Builder withJavaClasspath() {
            String[] classPath = System.getProperty("java.class.path").split(";");
            if (classPath.length >= 2) {
                for (String s : classPath) {
                    addUrl(
                            Path.of(
                                    s
                            )
                    );
                    System.out.println(s);
                }
            } else if (classPath.length == 1){
                addUrl(
                        Path.of(
                                classPath[0]
                        )
                );
            }
            return this;
        }

        public Builder addUrl(URL url) {
            this.urls.add(url);
            return this;
        }

        public Builder addUrl(Path path) {
            try {
                return addUrl(path.toUri().toURL());
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
        }

        public CustomizedClassloader build() {
            transformerFinder.loadDefaultTransformers(transformers);
            return new CustomizedClassloader(
                    urls.toArray(URL[]::new),
                    parent,
                    transformerFinder,
                    generators
            );
        }


        public CustomizedClassloader buildWithBootstrap() {
            CustomizedClassloader rcl = build();
            try (var cl = new URLClassLoader(urls.toArray(new URL[0]), rcl)) {
                Thread.currentThread().setContextClassLoader(cl);
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
            return rcl;
        }
    }
}
