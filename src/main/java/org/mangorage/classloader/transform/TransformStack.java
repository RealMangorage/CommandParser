package org.mangorage.classloader.transform;

import java.util.ArrayDeque;
import java.util.Deque;

public class TransformStack {

    public static TransformStack of(byte[] original) {
        return new TransformStack(original);
    }

    private final byte[] original;
    private Deque<byte[]> modified = new ArrayDeque<>();

    private TransformStack(byte[] original) {
        this.original = original;
    }

    public void push(byte[] bytes) {
        this.modified.push(bytes);
    }

    public byte[] getOriginal() {
        return original;
    }

    public byte[] getModified() {
        return modified.pop();
    }

    public byte[] getModifiedOrOriginal() {
        return modified.isEmpty() ? getOriginal() : getModified();
    }
}
