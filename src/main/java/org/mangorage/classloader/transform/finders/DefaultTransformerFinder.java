package org.mangorage.classloader.transform.finders;

import org.mangorage.classloader.misc.Utils;
import org.mangorage.classloader.transform.ITransformer;
import org.mangorage.classloader.transform.ITransformerFinder;

import java.util.List;

public class DefaultTransformerFinder implements ITransformerFinder {


    private final CachedTransformerList cache = new CachedTransformerList();
    private final String file;
    private boolean cached = false;

    public DefaultTransformerFinder(String file) {
        this.file = file;
    }

    @Override
    public List<ITransformer> findAndCacheTransformers() {
        if (cached)
            return cache.getCache();

        // Handle Searching
        Utils.findAllFilesInClasspath(file)
                .forEach(url -> {
                    System.out.println(url);
                });


        cached = true;
        return cache.getCache();
    }

    @Override
    public void loadDefaultTransformers(List<ITransformer> transformers) {
        cache.add(transformers);
    }
}
