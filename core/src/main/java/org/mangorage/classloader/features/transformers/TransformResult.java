package org.mangorage.classloader.features.transformers;

public record TransformResult(Class<?> transformer, String name, byte[] result) { }
