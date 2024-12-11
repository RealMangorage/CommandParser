package org.mangorage.testcl;

import org.mangorage.classloader.features.generators.IClassGenerator;
import org.mangorage.classloader.features.generators.UnbakedClass;
import javassist.bytecode.AccessFlag;

import java.io.IOException;
import java.lang.classfile.ClassFile;
import java.lang.constant.ClassDesc;
import java.lang.constant.ConstantDescs;
import java.lang.constant.MethodTypeDesc;
import java.lang.invoke.MethodHandles;
import java.nio.file.Files;
import java.nio.file.Path;

@SuppressWarnings("preview")
public class ClassGen implements IClassGenerator {
    public static byte[] getBytes(ClassDesc clazz) {
        ClassFile file = ClassFile.of();

        var bytes = file.build(
                clazz,
                cb -> {
                    cb.withMethod(
                            "<init>",
                            MethodTypeDesc.of(
                                    ConstantDescs.CD_void
                            ),
                            AccessFlag.PUBLIC,
                            mb -> {
                                mb.withCode(cb2 -> {

                                    // Load 'this' reference
                                    cb2.aload(0);

                                    // Call superclass constructor
                                    cb2.invokespecial(
                                            ConstantDescs.CD_Object,
                                            "<init>",
                                            MethodTypeDesc.of(
                                                    ConstantDescs.CD_void
                                            )
                                    );
                                    cb2.new_(
                                            ClassDesc.of(
                                                    "org.mangorage.testcl.TestingCL"
                                            )
                                    );

                                    cb2.dup();

                                    cb2.invokespecial(
                                            ClassDesc.of(
                                                    "org.mangorage.testcl.TestingCL"
                                            ),
                                            "<init>",
                                            MethodTypeDesc.of(
                                                    ConstantDescs.CD_void
                                            )
                                    );

                                    // Return
                                    cb2.return_();
                                });
                            }
                    );
                }
        );

        try {
            Files.write(
                    Path.of("test.class"),
                    bytes
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return bytes;
    }

    @Override
    public UnbakedClass generate(ClassFile classFile) {
        return new UnbakedClass(
                getBytes(ClassDesc.of("org.mangorage.testOk")),
                "org.mangorage.testOk"
        );
    }
}
