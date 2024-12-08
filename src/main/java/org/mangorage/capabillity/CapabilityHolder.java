package org.mangorage.capabillity;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mangorage.misc.LazyOptional;

import java.util.HashMap;
import java.util.Map;

public class CapabilityHolder<C> {
    private record CapKey<C>(CapabilityToken<?> token, C context) {}

    private static final Logger LOGGER = LogManager.getLogger();
    private final Map<CapKey<C>, LazyOptional<?>> caps = new HashMap<>();
    private final boolean hasContext;

    protected CapabilityHolder(boolean hasContext) {
        this.hasContext = hasContext;
    }

    public <B, T extends B> void register(@NotNull CapabilityToken<B> token, @NotNull T object, @Nullable C context) {
        if (!this.hasContext && context != null)
            context = null; // Cant pass through non-null if context is not allowed!
        if (this.caps.containsKey(new CapKey<>(token, context))) {
            LOGGER.warn("Already have a Capability Registered under same Token & Context");
            return;
        } else {
            caps.put(new CapKey<>(token, context), LazyOptional.of(() -> object));
        }

    }

    public <B, T extends B> void register(@NotNull CapabilityToken<B> token, @NotNull T object) {
        register(token, object, null);
    }

    public <T> LazyOptional<T> getCapability(@NotNull CapabilityToken<T> capabilityToken, C context) {
        if (!hasContext && context != null)
            context = null; // Cant pass through non-null if context is not allowed!
        var result = caps.get(new CapKey<>(capabilityToken, context));
        if (result.isPresent())
            return result.cast();
        return LazyOptional.empty(); // Its empty! No Capability!
    }


    public <T> LazyOptional<T> getCapability(@NotNull CapabilityToken<T> capabilityToken) {
        return getCapability(capabilityToken, null);
    }
}
