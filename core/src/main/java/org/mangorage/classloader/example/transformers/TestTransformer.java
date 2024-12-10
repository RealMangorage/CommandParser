package org.mangorage.classloader.example.transformers;



import org.mangorage.classloader.features.transformers.ITransformer;
import org.mangorage.classloader.features.transformers.TransformResult;
import org.mangorage.classloader.features.transformers.TransformStack;
import org.mangorage.classloader.misc.Utils;

import java.lang.classfile.ClassFile;
import java.lang.classfile.ClassModel;
import java.lang.classfile.ClassTransform;
import java.lang.classfile.MethodTransform;
import java.lang.constant.ClassDesc;
import java.lang.constant.ConstantDescs;
import java.lang.constant.DynamicCallSiteDesc;
import java.lang.constant.MethodTypeDesc;
import java.lang.invoke.StringConcatFactory;
import java.util.Objects;

@SuppressWarnings("preview")
public class TestTransformer implements ITransformer {
    @Override
    public void transform(ClassFile classFile, TransformStack stack) {
        ClassModel classModel = classFile.parse(stack.getOriginal());

        var clazz = classFile.transform(
                classModel,
                ClassTransform.ofStateful(
                        () -> ClassTransform
                                .transformingMethods(it -> it.methodName().equalsString("create"),
                                        MethodTransform.endHandler(mb -> {
                                            mb.withCode(cb -> {
                                                System.out.println("LOOOL");
                                                var name = cb.parameterSlot(0);
                                                var name2 = cb.parameterSlot(1);
                                                var name3 = cb.parameterSlot(2);
                                                cb.aload(name);
                                                cb.aload(name2);
                                                cb.aload(name3);
                                                cb.invokedynamic(
                                                        DynamicCallSiteDesc.of(
                                                                ConstantDescs.ofCallsiteBootstrap(
                                                                        StringConcatFactory.class.describeConstable().orElseThrow(),
                                                                        "makeConcatWithConstants",
                                                                        ConstantDescs.CD_CallSite,
                                                                        ConstantDescs.CD_String,
                                                                        ConstantDescs.CD_Object.arrayType()
                                                                ),
                                                                "makeConcatWithConstants",
                                                                MethodTypeDesc.of(
                                                                        ConstantDescs.CD_String,
                                                                        ConstantDescs.CD_String,
                                                                        ConstantDescs.CD_String,
                                                                        ConstantDescs.CD_String
                                                                ),
                                                                "\u0001 LOL \u0001 \u0001"
                                                        )
                                                );
                                                cb.areturn();
                                            });
                                        })
                                )
                ).andThen(
                        ClassTransform.transformingMethods(it -> it.methodName().equalsString("createCombined"),
                                MethodTransform.endHandler(mb -> {
                                    mb.withCode(cb -> {
                                        Utils.createRecordByteCode(cb, ClassDesc.of("org.mangorage.testcl.asmstuff.Combined"), 3, ConstantDescs.CD_String, ConstantDescs.CD_String, ConstantDescs.CD_String);
                                    });
                                })
                        )
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
    public boolean handlesClass(String clazz) {
        return Objects.equals(clazz, "org.mangorage.testcl.asmstuff.Test");
    }

    @Override
    public String name() {
        return "Basic Transformer";
    }
}
