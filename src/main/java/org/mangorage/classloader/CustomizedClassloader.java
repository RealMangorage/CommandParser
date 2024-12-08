package org.mangorage.classloader;

import org.mangorage.classloader.transform.ITransformer;
import org.mangorage.classloader.transform.TransformStack;

import java.io.IOException;
import java.io.InputStream;
import java.lang.classfile.ClassBuilder;
import java.lang.classfile.ClassFile;
import java.lang.classfile.ClassModel;
import java.lang.constant.ClassDesc;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

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

    private final Map<String, Class<?>> classMap = new HashMap<>();
    private final List<ITransformer> transformers;

    public CustomizedClassloader(URL[] urls, ClassLoader parent, List<ITransformer> transformers) {
        super(urls, parent);
        this.transformers = List.copyOf(transformers);
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
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        if (transformers.isEmpty())
            return findAndStoreClass(name);
        if (classMap.containsKey(name))
            return classMap.get(name);

        try {
            List<ITransformer> transformersRemain = transformers.stream()
                    .filter(t -> t.handlesClass(name))
                    .toList();

            if (transformersRemain.isEmpty())
                return findAndStoreClass(name);

            byte[] original = getClassBytes(name);
            if (original == null)
                throw new IllegalStateException("Class Bytes were null for class " + name);

            TransformStack stack = TransformStack.of(original);
            transformersRemain.forEach(t -> t.transform(stack));

            Class<?> clazz = defineClass(name, stack.getModifiedOrOriginal());
            classMap.put(name, clazz);
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
        private final List<URL> urls = new ArrayList<>();
        private final ClassLoader parent;

        private Builder(ClassLoader parent) {
            this.parent = parent;
        }

        public Builder addTransformer(ITransformer transformer) {
            this.transformers.add(transformer);
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
            return new CustomizedClassloader(
                    urls.toArray(URL[]::new),
                    parent,
                    transformers
            );
        }

    }
}
