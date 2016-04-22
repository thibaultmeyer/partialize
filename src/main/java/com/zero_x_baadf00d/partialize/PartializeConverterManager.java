package com.zero_x_baadf00d.partialize;

import com.zero_x_baadf00d.partialize.converter.Converter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This manager handles all registered converters.
 *
 * @author Thibault Meyer
 * @version 16.03.22
 * @since 16.03.22
 */
public final class PartializeConverterManager {

    /**
     * Handle to current enabled registeredConverters.
     *
     * @since 16.03.22
     */
    private final Map<Class, Converter> registeredConverters;

    /**
     * Build a basic instance.
     *
     * @since 16.03.22
     */
    private PartializeConverterManager() {
        this.registeredConverters = new HashMap<>();
    }

    /**
     * Get current instance.
     *
     * @return The current instance of {@code PartializeConverterManager}
     * @since 16.03.22
     */
    public static PartializeConverterManager getInstance() {
        return PartializeConverterManagerSingletonHolder.INSTANCE;
    }

    /**
     * Register new converter to Partialize.
     *
     * @param converter The converter to register
     * @since 16.03.22
     */
    public void registerConverter(final Converter converter) {
        this.registeredConverters.putIfAbsent(converter.getManagedObjectClass(), converter);
    }

    /**
     * Register new registeredConverters to Partialize.
     *
     * @param converters A list of registeredConverters to register
     * @since 16.03.22
     */
    public void registerConverters(final Converter... converters) {
        for (final Converter converter : converters) {
            this.registeredConverters.putIfAbsent(converter.getManagedObjectClass(), converter);
        }
    }

    /**
     * Register new registeredConverters to Partialize.
     *
     * @param converters A list of registeredConverters to register
     * @since 16.03.22
     */
    public void registerConverters(final List<Converter> converters) {
        for (final Converter converter : converters) {
            this.registeredConverters.putIfAbsent(converter.getManagedObjectClass(), converter);
        }
    }

    /**
     * Get a registered converter.
     *
     * @param clazz The class of the object to convert
     * @return The converter, otherwise, {@code null}
     * @since 16.03.22
     */
    public Converter getConverter(final Class clazz) {
        return this.registeredConverters.get(clazz);
    }

    /**
     * Single holder for {@code PartializeConverterManager}.
     *
     * @author Thibault Meyer
     * @version 16.03.22
     * @see PartializeConverterManager
     * @since 16.03.22
     */
    private static class PartializeConverterManagerSingletonHolder {
        private final static PartializeConverterManager INSTANCE = new PartializeConverterManager();
    }
}
