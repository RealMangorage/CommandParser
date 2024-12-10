package org.mangorage.classloader.features.locators;

import org.mangorage.classloader.features.transformers.ITransformer;

import java.util.List;

public interface ITransformerLocator {
    List<ITransformer> findAndCacheTransformers();
    void loadDefaultTransformers(List<ITransformer> transformers); // Add default Transformers
}
