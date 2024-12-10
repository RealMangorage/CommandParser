package org.mangorage.testcl.example.transformers;

import org.mangorage.classloader.features.transformers.ITransformer;
import org.mangorage.classloader.features.transformers.TransformResult;
import org.mangorage.classloader.features.transformers.TransformStack;
import org.mangorage.testcl.MathOperationFactory;

import java.io.IOException;
import java.lang.classfile.ClassFile;
import java.lang.classfile.ClassModel;
import java.lang.classfile.ClassTransform;
import java.lang.classfile.MethodTransform;
import java.lang.constant.ConstantDescs;
import java.lang.constant.DynamicCallSiteDesc;
import java.lang.constant.MethodTypeDesc;
import java.lang.invoke.StringConcatFactory;
import java.nio.file.Files;
import java.nio.file.Path;

@SuppressWarnings("preview")
public class MathTestTransformer implements ITransformer {

    @Override
    public void transform(ClassFile classFile, TransformStack stack) {
        ClassModel classModel = ClassFile.of().parse(stack.getOriginal());

        var clazz = ClassFile.of().transform(
                classModel,
                ClassTransform.ofStateful(
                        () -> ClassTransform
                                .transformingMethods(it -> it.methodName().equalsString("calculate"),
                                        MethodTransform.endHandler(mb -> {
                                            mb.withCode(cb -> {
                                                System.out.println("LOOOL");
                                                var name = cb.parameterSlot(0);
                                                var name2 = cb.parameterSlot(1);
                                                cb.iload(name);
                                                cb.iload(name2);

                                                cb.invokedynamic(
                                                        DynamicCallSiteDesc.of(
                                                                ConstantDescs.ofCallsiteBootstrap(
                                                                        MathOperationFactory.class.describeConstable().orElseThrow(),
                                                                        "makeMathOperationCallsite",
                                                                        ConstantDescs.CD_CallSite,
                                                                        ConstantDescs.CD_Class,
                                                                        ConstantDescs.CD_String
                                                                ),
                                                                "makeMathOperationCallsite",
                                                                MethodTypeDesc.of(
                                                                        ConstantDescs.CD_int,
                                                                        ConstantDescs.CD_int,
                                                                        ConstantDescs.CD_int
                                                                ),
                                                                Math.class.describeConstable().orElseThrow(),
                                                                "subtractExact"
                                                        )
                                                );
                                                cb.ireturn();
                                            });
                                        })
                                )
                )
        );

        ClassFile.of().verify(clazz);

        try {
            Files.write(
                    Path.of("testMathy3.class"),
                    clazz
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        stack.push(
                new TransformResult(
                        getClass(),
                        name(),
                        clazz
                )
        );
    }

    @Override
    public boolean handlesClass(String clazz) {
        return clazz.equals("org.mangorage.testcl.asmstuff.MathTest");
    }

    @Override
    public String name() {
        return "MathTest Transformer";
    }
}
