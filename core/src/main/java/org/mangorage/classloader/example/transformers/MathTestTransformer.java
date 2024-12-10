package org.mangorage.classloader.example.transformers;

import org.mangorage.classloader.features.transformers.ITransformer;
import org.mangorage.classloader.features.transformers.TransformResult;
import org.mangorage.classloader.features.transformers.TransformStack;
import java.io.IOException;
import java.lang.classfile.ClassFile;
import java.lang.classfile.ClassModel;
import java.lang.classfile.ClassTransform;
import java.lang.classfile.MethodTransform;
import java.lang.constant.ConstantDescs;
import java.lang.constant.MethodTypeDesc;
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

                                                cb.aload(name);
                                                cb.invokevirtual(
                                                        ConstantDescs.CD_Integer,
                                                        "intValue",
                                                        MethodTypeDesc.of(
                                                                ConstantDescs.CD_int
                                                        )
                                                );

                                                cb.aload(name2);
                                                cb.invokevirtual(
                                                        ConstantDescs.CD_Integer,
                                                        "intValue",
                                                        MethodTypeDesc.of(
                                                                ConstantDescs.CD_int
                                                        )
                                                );
                                                cb.iadd();

                                                cb.invokestatic(
                                                        ConstantDescs.CD_Integer,
                                                        "valueOf",
                                                        MethodTypeDesc.of(
                                                                ConstantDescs.CD_Integer,
                                                                ConstantDescs.CD_int
                                                        )
                                                );

                                                cb.areturn();
                                            });
                                        })
                                )
                )
        );

        ClassFile.of().verify(clazz);

        try {
            Files.write(
                    Path.of("test.class"),
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
