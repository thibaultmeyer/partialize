package com.zero_x_baadf00d.partialize.policy;

/**
 * AccessPolicy.
 *
 * @author Thibault Meyer
 * @version 16.02
 * @since 16.02
 */
public class AccessPolicy {

    /**
     * The class type.
     *
     * @since 16.02
     */
    public Class<?> clazz;

    /**
     * The class instance.
     *
     * @since 16.02
     */
    public Object instance;

    /**
     * The method that will be called.
     *
     * @since 16.02
     */
    public String method;

    /**
     * Build a standard instance.
     *
     * @param clazz    The class type
     * @param instance The class instance
     * @param method   The method that will be called
     * @since 16.02
     */
    public AccessPolicy(final Class<?> clazz, final Object instance, final String method) {
        this.clazz = clazz;
        this.instance = instance;
        this.method = method;
    }
}
