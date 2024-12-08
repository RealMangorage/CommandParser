package org.mangorage.classloader.transform;

import java.util.List;

public record TransformedClass(Class<?> modifiedClass, List<TransformResult> results) {}
