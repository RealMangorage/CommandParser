package org.mangorage.classloader.features.locators;

import org.mangorage.classloader.features.transformers.ITransformer;

import java.util.List;

public final class JavaTransformerLocator implements ITransformerLocator {
    private final CachedTransformerList cache = new CachedTransformerList();

    @Override
    public List<ITransformer> findAndCacheTransformers() {
        return cache.getCache();
    }

    @Override
    public void loadDefaultTransformers(List<ITransformer> transformers) {
        cache.add(transformers);
    }
}
