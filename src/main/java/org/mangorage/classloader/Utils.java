package org.mangorage.classloader;

import java.lang.classfile.CodeBuilder;
import java.lang.constant.ClassDesc;
import java.lang.constant.ConstantDescs;
import java.lang.constant.MethodTypeDesc;
import java.util.ArrayList;
import java.util.List;

public class Utils {
    public static void createRecordByteCode(CodeBuilder cb, ClassDesc clazz, int parameters, ClassDesc... descs) {
        cb.new_(
                clazz
        );
        cb.dup();
        cb.aload(1);
        cb.aload(2);
        cb.aload(3);

        cb.invokespecial(
                clazz,
                "<init>",
                MethodTypeDesc.of(
                        ConstantDescs.CD_void,
                        descs
                )
        );
        cb.areturn();
    }
}