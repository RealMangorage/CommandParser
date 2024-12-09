package org.mangorage.classloader.misc;

import java.lang.classfile.CodeBuilder;
import java.lang.constant.ClassDesc;
import java.lang.constant.ConstantDescs;
import java.lang.constant.MethodTypeDesc;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.stream.Stream;

@SuppressWarnings("preview")
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

    public static List<URL> findAllFilesInClasspath(String fileName) {
        try {
            Enumeration<URL> resources = ClassLoader.getSystemClassLoader().getResources(fileName);

            return Stream.of(resources)
                    .filter(Enumeration::hasMoreElements)
                    .map(Enumeration::nextElement)
                    .toList();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

}