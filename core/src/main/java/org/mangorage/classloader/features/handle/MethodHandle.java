package org.mangorage.classloader.features.handle;

public abstract class MethodHandle<R> {
    private final String clazz;
    private final String method;
    private final boolean isStatic;
    private final Class<R> returnType;

    public MethodHandle(String clazz, String method, boolean isStatic, Class<R> returnType) {
        this.clazz = clazz;
        this.method = method;
        this.isStatic = isStatic;
        this.returnType = returnType;
    }

    @SuppressWarnings("all")
    public final R invoke(Object instance, Object[] parameters) {
        return (R) invokeInternal(
                clazz,
                method,
                isStatic,
                instance,
                parameters
        );
    }

    private Object invokeInternal(String clazz, String method, boolean isStatic, Object instance, Object[] parameters) {
        throw new IllegalStateException("NOT IMPLEMENTED");
    }
}
