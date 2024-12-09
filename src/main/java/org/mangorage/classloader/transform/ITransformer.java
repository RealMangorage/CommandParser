package org.mangorage.classloader.transform;

public interface ITransformer {
    void transform(TransformStack stack);

    boolean handlesClass(String clazz);
    String name();
}
