package org.mangorage.classloader.features.transformers;

import java.lang.classfile.ClassFile;

@SuppressWarnings("preview")
public interface ITransformer {
    void transform(ClassFile classFile, TransformStack stack);

    boolean handlesClass(String clazz);
    String name();
}
