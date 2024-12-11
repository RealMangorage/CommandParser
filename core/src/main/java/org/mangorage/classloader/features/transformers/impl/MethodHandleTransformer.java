package org.mangorage.classloader.features.transformers.impl;

import org.mangorage.classloader.features.transformers.ITransformer;
import org.mangorage.classloader.features.transformers.TransformStack;

import java.lang.classfile.ClassFile;
import java.lang.classfile.ClassModel;

@SuppressWarnings("preview")
public class MethodHandleTransformer implements ITransformer {

    @Override
    public void transform(ClassFile classFile, TransformStack stack) {
        var a = classFile.parse(stack.getOriginal());
       Object b = null;
    }

    @Override
    public boolean handlesClass(ClassModel clazz) {
        return false;
    }

    @Override
    public String name() {
        return "Method Handle Transformer";
    }
}
