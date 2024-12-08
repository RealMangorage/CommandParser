package org.mangorage.classloader.transform;

public record TransformResult(Class<?> transformer, String name, byte[] result) { }
