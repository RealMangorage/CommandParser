package org.mangorage.classloader.features.locators;

import org.mangorage.classloader.features.transformers.ITransformer;
import org.mangorage.classloader.misc.Utils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class DefaultTransformerLocator implements ITransformerLocator {


    private final CachedTransformerList cache = new CachedTransformerList();
    private final String file;
    private boolean cached = false;

    public DefaultTransformerLocator(String file) {
        this.file = file;
    }

    @Override
    public List<ITransformer> findAndCacheTransformers() {
        if (cached)
            return cache.getCache();

        this.cached = true;

        // Handle Searching
        Utils.getAllFileContent(file)
                .forEach(url -> {
                    System.out.println("""
                            Attempting to load Transformers found in
                            %s
                            """.formatted(url));

                    for (String line : url.split("\n")) {
                        if (line.startsWith("transformer")) {
                            var transformerName = line.substring(12);
                            System.out.println("Found %s".formatted(transformerName));
                            try {
                                var transformer = Class.forName(
                                        transformerName
                                );
                                if (ITransformer.class.isAssignableFrom(transformer)) {
                                    System.out.println("""
                                            Successfully loaded Transformer:
                                            %s
                                            
                                            located at
                                            %s
                                            """
                                            .formatted(
                                                    transformerName,
                                                    url
                                            )
                                    );

                                    cache.add(
                                            (ITransformer) transformer.newInstance()
                                    );
                                } else {
                                    System.out.println("""
                                            Failed to load Transformer:
                                            %s
                                            
                                            located at
                                            %s
                                            
                                            Must implement %s
                                            """
                                            .formatted(
                                                    transformerName,
                                                    url,
                                                    ITransformer.class.getName()
                                            )
                                    );
                                }
                            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                                System.out.println("""
                                        Failed to load Transformer:
                                        %s
                                        
                                        located at
                                        %s
                                        """
                                        .formatted(
                                                transformerName,
                                                url
                                        )
                                );
                            }
                        }
                    }
                });
        return cache.getCache();
    }

    @Override
    public void loadDefaultTransformers(List<ITransformer> transformers) {
        cache.add(transformers);
    }
}
