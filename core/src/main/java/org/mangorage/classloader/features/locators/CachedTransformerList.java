package org.mangorage.classloader.features.locators;

import org.mangorage.classloader.features.transformers.ITransformer;

import java.util.ArrayList;
import java.util.List;

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
