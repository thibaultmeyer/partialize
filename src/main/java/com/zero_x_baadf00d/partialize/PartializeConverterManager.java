/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 Thibault Meyer
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.zero_x_baadf00d.partialize;

import com.zero_x_baadf00d.partialize.converter.Converter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This manager handles all registered converters.
 *
 * @author Thibault Meyer
 * @version 16.10.04
 * @since 16.03.22
 */
public final class PartializeConverterManager {

    /**
     * Handle to current enabled converters.
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
     * Register a new converter to Partialize.
     *
     * @param converter The converter to register
     * @since 16.03.22
     */
    public void registerConverter(final Converter converter) {
        this.registeredConverters.putIfAbsent(converter.getManagedObjectClass(), converter);
    }

    /**
     * Register new converters to Partialize.
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
     * Register new converters to Partialize.
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
     * Unregister all converters.
     *
     * @since 16.10.04
     */
    public void removeAllConverters() {
        this.registeredConverters.clear();
    }

    /**
     * Get the number of registered converters.
     *
     * @return The number of registered converters
     * @since 16.10.04
     */
    public int count() {
        return this.registeredConverters.size();
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
