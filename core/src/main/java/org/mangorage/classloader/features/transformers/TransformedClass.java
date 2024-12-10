package org.mangorage.classloader.features.transformers;

import java.util.List;

public record TransformedClass(Class<?> modifiedClass, List<TransformResult> results) {}
