package org.mangorage.classloader.example;

import org.mangorage.classloader.Utils;
import org.mangorage.classloader.transform.ITransformer;
import org.mangorage.classloader.transform.TransformResult;
import org.mangorage.classloader.transform.TransformStack;

import java.io.IOException;
import java.lang.classfile.ClassFile;
import java.lang.classfile.ClassModel;
import java.lang.classfile.ClassTransform;
import java.lang.classfile.MethodTransform;
import java.lang.constant.ClassDesc;
import java.lang.constant.ConstantDescs;
import java.lang.constant.DynamicCallSiteDesc;
import java.lang.constant.MethodTypeDesc;
import java.lang.invoke.StringConcatFactory;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

public class TestTransformer implements ITransformer {
    @Override
    public void transform(TransformStack stack) {
        ClassModel classModel = ClassFile.of().parse(stack.getOriginal());

        var clazz = ClassFile.of().transform(
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
                                        Utils.createRecordByteCode(cb, ClassDesc.of("org.mangorage.testcl.Combined"), 3, ConstantDescs.CD_String, ConstantDescs.CD_String, ConstantDescs.CD_String);
                                    });
                                })
                        )
                )
        );

        ClassFile.of().verify(clazz);

        try {
            Files.write(
                    Path.of("Test.class"),
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
        return Objects.equals(clazz, "org.mangorage.testcl.Test");
    }

    @Override
    public String name() {
        return "Basic Transformer";
    }
}
