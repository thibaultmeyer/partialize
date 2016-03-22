package com.zero_x_baadf00d.partialize;

import com.zero_x_baadf00d.partialize.converter.Converter;

import java.util.HashMap;
import java.util.Map;

/**
 * PartializeConverterManager.
 *
 * @author Thibault Meyer
 * @version 16.03.22
 * @since 16.03.22
 */
final class PartializeConverterManager {

    /**
     * Handle to current enabled converters.
     *
     * @since 16.03.22
     */
    private final Map<Class, Converter> converters;

    /**
     * Build a basic instance.
     *
     * @since 16.03.22
     */
    private PartializeConverterManager() {
        this.converters = new HashMap<>();
    }

    /**
     * Get current instance.
     *
     * @return The current instance of {@code PartializeConverterManager}
     * @since 16.03.22
     */
    static PartializeConverterManager getInstance() {
        return PartializeConverterManagerSingletonHolder.INSTANCE;
    }

    /**
     * Register new converter to Partialize.
     *
     * @param converter The converter to register
     * @since 16.03.22
     */
    void registerConverter(final Converter converter) {
        this.converters.putIfAbsent(converter.getManagedObjectClass(), converter);
    }

    /**
     * Get a registered converter.
     *
     * @param clazz The class of the object to convert
     * @return The converter, otherwise, {@code null}
     */
    Converter getConverter(final Class clazz) {
        return this.converters.get(clazz);
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
