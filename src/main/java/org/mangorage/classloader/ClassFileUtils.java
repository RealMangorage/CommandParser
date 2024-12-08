package org.mangorage.classloader;

import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.classfile.AccessFlags;
import java.lang.constant.ClassDesc;
import java.lang.constant.MethodTypeDesc;
import java.lang.reflect.AccessFlag;
import java.util.Arrays;

public class ClassFileUtils {
    public static void writeBytesToFile(byte[] data, String filePath) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            fos.write(data);
        }
    }

    public static String parseSignature(String signature) {
        int start = signature.indexOf('<');
        if (start == -1) {
            return null;
        }

        int end = signature.indexOf('>');
        if (end == -1) {
            return null;
        }

        return signature.substring(start + 1, end - 1);
    }


    public static ClassDesc getClassName(Class<?> clazz) {
        return ClassDesc.ofDescriptor(clazz.descriptorString());
    }

    public static MethodTypeDesc getMethodDesc(Class<?> returnType, Class<?>... args) {
        var paramList = Arrays.stream(args)
                .map(ClassFileUtils::getClassName)
                .toList();
        if (paramList.isEmpty()) {
            return MethodTypeDesc.of(
                    getClassName(returnType)
            );
        } else {
            return MethodTypeDesc.of(
                    getClassName(returnType),
                    paramList
            );
        }
    }

    public static int getMethodFlagsInt(AccessFlag... flags) {
        return AccessFlags.ofMethod(flags).flagsMask();
    }
}