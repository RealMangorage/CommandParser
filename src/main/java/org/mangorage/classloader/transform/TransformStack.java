package org.mangorage.classloader.transform;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

public class TransformStack {

    public static TransformStack of(byte[] original) {
        return new TransformStack(original);
    }

    private final byte[] original;
    private Deque<TransformResult> modified = new ArrayDeque<>();

    private TransformStack(byte[] original) {
        this.original = original;
    }

    public void push(TransformResult result) {
        this.modified.push(result);
    }

    public byte[] getOriginal() {
        return original;
    }

    public TransformResult getModified() {
        return modified.peek();
    }

    public byte[] getModifiedOrOriginal() {
        return modified.isEmpty() ? getOriginal() : getModified().result();
    }

    public List<TransformResult> getHistory() {
        return modified.stream().toList();
    }
}
