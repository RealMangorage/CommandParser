package org.mangorage.classloader.transform.finders;

import org.mangorage.classloader.transform.ITransformer;
import org.mangorage.classloader.transform.ITransformerFinder;

import java.util.Collections;
import java.util.List;

public final class EmptyTransformerFinder implements ITransformerFinder {
    public static final EmptyTransformerFinder INSTANCE = new EmptyTransformerFinder();


    private final List<ITransformer> EMPTY = Collections.emptyList();

    private EmptyTransformerFinder() {}

    @Override
    public List<ITransformer> findAndCacheTransformers() {
        return EMPTY;
    }

    @Override
    public void loadDefaultTransformers(List<ITransformer> transformers) {
    }
}
