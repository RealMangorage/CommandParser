package org.mangorage.boostrap;

import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.util.Arrays;

public class Bootstrap {
    public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        var urls = Arrays.stream(args[0].split(";"))
                .map(l -> {
                    try {
                        return Path.of(l).toUri().toURL();
                    } catch (MalformedURLException e) {
                        throw new RuntimeException(e);
                    }
                }).toArray(URL[]::new);
        ClassLoader parent = Thread.currentThread().getContextClassLoader().getParent();
        URLClassLoader urlClassLoader = new URLClassLoader(urls, parent);
        Thread.currentThread().setContextClassLoader(urlClassLoader);
        Class.forName(args[1], false, urlClassLoader).getDeclaredMethod("main", String[].class).invoke(null, (Object) new String[]{});
    }
}
