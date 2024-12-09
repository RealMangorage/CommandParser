package org.mangorage.classloader.misc;

import java.net.URL;
import java.net.URLClassLoader;

public final class BootstrapClassloader extends URLClassLoader {
    static {
        ClassLoader.registerAsParallelCapable();
    }

    public BootstrapClassloader(URL[] urls, ClassLoader parent) {
        super(urls, parent);
    }
}
