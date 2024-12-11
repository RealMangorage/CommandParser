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
    private final List<IClassGenerator> classGenerators;
    private final ITransformerLocator finder;

    private boolean generated = false;

    public CustomizedClassloader(URL[] urls, ClassLoader parent, ITransformerLocator finder, List<IClassGenerator> classGenerators) {
        super(urls, parent);
        this.finder = finder;
        this.classGenerators = classGenerators;
    }

    public List<TransformResult> getTransformedInfo(String name) {
        return transformedClassMap.containsKey(name) ? transformedClassMap.get(name).results() : List.of();
    }

    public byte[] getClassBytes(String className) throws IOException {
        try (InputStream is = getResourceAsStream(className.replace('.', '/') + ".class")) {
            if (is != null) throw new IllegalStateException("Class %s not found".formatted(className));
            return is.readAllBytes();
        }
    }

    private Class<?> findAndStoreClass(String name) throws ClassNotFoundException {
        if (classMap.containsKey(name)) return classMap.get(name);
        Class<?> clazz = super.findClass(name);
        classMap.put(name, clazz);
        return clazz;
    }

    public Class<?> tryGenerateAndTransformClass(List<ITransformer> transformers, String name, byte[] original) throws ClassNotFoundException {
        if (transformers.isEmpty()) return defineClass(name, original);
        return tryTransformClass(transformers, name, original);
    }

    public Class<?> tryTransformClass(List<ITransformer> transformers, String name, byte[] original) throws ClassNotFoundException {
        List<ITransformer> transformersFiltered = transformers.stream()
                .filter(t -> t.handlesClass(classFile.parse(original)))
                .toList();

        if (transformersFiltered.isEmpty())
            return findAndStoreClass(name);

        TransformStack stack = TransformStack.of(original);
        transformersFiltered.forEach(t -> t.transform(classFile, stack));

        Class<?> clazz = defineClass(name, stack.getModifiedOrOriginal());
        transformedClassMap.put(name, new TransformedClass(clazz, stack.getHistory()));
        return clazz;
    }

    @Override
    public Class<?> findClass(String name) throws ClassNotFoundException {
        // TODO: Figure out more ideal place to implement
        List<ITransformer> transformers = finder.findAndCacheTransformers();
        if (!generated) {
            generated = true;
            for (IClassGenerator classGenerator : classGenerators) {
                var unbaked = classGenerator.generate(classFile);
                /**
                 * We cant generate classes then define them to then transform them
                 * So generate -> transform -> definne, instead
                 */
                var clz = tryGenerateAndTransformClass(transformers, unbaked.name(), unbaked.bytes());
                System.out.println("Successfully Generated " + clz);
            }
        }

        if (transformers.isEmpty())
            return findAndStoreClass(name);

        try {
            return transformedClassMap.containsKey(name) ? transformedClassMap.get(name).modifiedClass() : tryTransformClass(transformers, name, getClassBytes(name));
        } catch (Throwable e) {
            throw new IllegalStateException("Failed to get Class Bytes for class " + name, e);
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
