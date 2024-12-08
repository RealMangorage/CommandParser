package org.mangorage.classloader.transform;

public interface ITransformer {
    void transform(TransformStack classData);

    boolean handlesClass(String clazz);
    String name();
}
