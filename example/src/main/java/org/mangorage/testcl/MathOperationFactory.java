package org.mangorage.testcl;

import java.lang.invoke.CallSite;
import java.lang.invoke.ConstantCallSite;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

public class MathOperationFactory {
    public static CallSite makeMathOperationCallsite(MethodHandles.Lookup lookup,
                                                      String name,
                                                      MethodType concatType,
                                                      Class<?> clazz,
                                                      String func
    ) throws NoSuchMethodException, IllegalAccessException {
        System.out.println(
                name
        );
        System.out.println(
                clazz
        );
        System.out.println(
                func
        );
        MethodType methodType = MethodType.methodType(int.class, int.class, int.class);
        MethodHandle addHandle = lookup.findStatic(clazz, func , methodType);
        return new ConstantCallSite(addHandle);
    }
}
