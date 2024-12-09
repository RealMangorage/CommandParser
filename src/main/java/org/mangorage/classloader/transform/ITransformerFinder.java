package org.mangorage.classloader.transform;

import java.util.List;

public interface ITransformerFinder {
    List<ITransformer> findAndCacheTransformers();
    void loadDefaultTransformers(List<ITransformer> transformers); // Add default Transformers
}
