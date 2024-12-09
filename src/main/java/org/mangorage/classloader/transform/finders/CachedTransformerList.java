package org.mangorage.classloader.transform.finders;

import org.mangorage.classloader.transform.ITransformer;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class CachedTransformerList {
    private final List<ITransformer> transformersMutable = new ArrayList<>();
    private final Object lock = new Object();
    private List<ITransformer> cache = List.of();

    public void add(List<ITransformer> transformers) {
        transformersMutable.addAll(transformers);
        synchronized (lock) {
            this.cache = List.copyOf(transformersMutable);
        }
    }

    public void add(ITransformer transformer) {
        transformersMutable.add(transformer);
        synchronized (lock) {
            this.cache = List.copyOf(transformersMutable);
        }
    }

    public List<ITransformer> getCache() {
        synchronized (lock) {
            return cache;
        }
    }
}
