package org.mangorage.classloader.features.transformers.impl;

import org.mangorage.classloader.features.transformers.ITransformer;
import org.mangorage.classloader.features.transformers.TransformResult;
import org.mangorage.classloader.features.transformers.TransformStack;
import java.lang.classfile.ClassFile;
import java.lang.classfile.ClassModel;
import java.lang.classfile.ClassTransform;
import java.lang.constant.ClassDesc;

@SuppressWarnings("preview")
public class InterfaceTransformer implements ITransformer {

    private final ClassDesc interfaceDesc;
    private final ClassDesc targetDesc;

    public InterfaceTransformer(ClassDesc interfaceDesc, ClassDesc targetDesc) {
        this.interfaceDesc = interfaceDesc;
        this.targetDesc = targetDesc;
    }


    @Override
    public void transform(ClassFile classFile, TransformStack stack) {
        ClassModel classModel = ClassFile.of().parse(stack.getOriginal());

        var clazz = ClassFile.of().transform(
                classModel,
                ClassTransform.ofStateful(
                        () -> ClassTransform.endHandler(cb -> {
                            cb.withInterfaceSymbols(
                                    interfaceDesc
                            );
                        })
                )
        );

        ClassFile.of().verify(clazz);

        stack.push(
                new TransformResult(
                        getClass(),
                        name(),
                        clazz
                )
        );
    }

    @Override
    public boolean handlesClass(ClassModel clazz) {
        return clazz.thisClass().asSymbol().equals(targetDesc);
    }

    @Override
    public String name() {
        return "Method Handle Transformer";
    }
}
