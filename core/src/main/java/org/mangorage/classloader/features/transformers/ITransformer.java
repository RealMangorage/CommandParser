package org.mangorage.classloader.features.transformers;

import java.lang.classfile.ClassFile;
import java.lang.classfile.ClassModel;

@SuppressWarnings("preview")
public interface ITransformer {
    void transform(ClassFile classFile, TransformStack stack);

    boolean handlesClass(ClassModel classModel);
    String name();
}
