package org.mangorage.classloader.features.locators;

import org.mangorage.classloader.features.transformers.ITransformer;

import java.util.Collections;
import java.util.List;

public final class EmptyTransformerLocator implements ITransformerLocator {
    public static final EmptyTransformerLocator INSTANCE = new EmptyTransformerLocator();


    private final List<ITransformer> EMPTY = Collections.emptyList();

    private EmptyTransformerLocator() {}

    @Override
    public List<ITransformer> findAndCacheTransformers() {
        return EMPTY;
    }

    @Override
    public void loadDefaultTransformers(List<ITransformer> transformers) {
    }
}
