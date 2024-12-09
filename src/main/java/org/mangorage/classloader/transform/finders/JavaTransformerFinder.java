package org.mangorage.classloader.transform.finders;

import org.mangorage.classloader.transform.ITransformer;
import org.mangorage.classloader.transform.ITransformerFinder;

import java.util.List;

public final class JavaTransformerFinder implements ITransformerFinder {
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
