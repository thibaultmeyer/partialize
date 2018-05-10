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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ContainerNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.zero_x_baadf00d.partialize.converter.Converter;
import com.zero_x_baadf00d.partialize.policy.AccessPolicy;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.WordUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Create a partial JSON document from any kind of objects.
 *
 * @author Thibault Meyer
 * @version 18.05.10
 * @since 16.01.18
 */
public class Partialize {

    /**
     * Default maximum reachable depth level.
     *
     * @since 16.01.18
     */
    private static final int DEFAULT_MAXIMUM_DEPTH = 64;

    /**
     * Default scanner delimiter pattern.
     *
     * @since 16.01.18
     */
    private static final String SCANNER_DELIMITER = ",";

    /**
     * Method prefixes.
     *
     * @since 17.06.28
     */
    private static final String[] METHOD_PREFIXES = {"get", "is", "has", "can"};

    /**
     * Pattern used to extract arguments.
     *
     * @since 16.01.18
     */
    private final Pattern fieldArgsPattern = Pattern.compile("([a-zA-Z0-9]{1,})\\((.+)\\)");

    /**
     * Object mapper used to create new object nodes.
     *
     * @since 16.01.18
     */
    private final ObjectMapper objectMapper;

    /**
     * The maximum reachable depth level.
     *
     * @since 16.01.18
     */
    private final int maximumDepth;

    /**
     * The access policy function.
     *
     * @since 16.02.13
     */
    private Function<AccessPolicy, Boolean> accessPolicyFunction;

    /**
     * Defined aliases.
     *
     * @since 16.03.11
     */
    private Map<String, String> aliases;

    /**
     * Exception function.
     *
     * @since 16.03.15
     */
    private Consumer<Exception> exceptionConsumer;

    /**
     * Build a default instance.
     *
     * @since 16.01.18
     */
    public Partialize() {
        this(com.zero_x_baadf00d.partialize.Partialize.DEFAULT_MAXIMUM_DEPTH);
    }

    /**
     * Build an instance with a specific maximum depth value set.
     *
     * @param maximumDepth Maximum allowed depth value to set
     * @since 16.01.18
     */
    public Partialize(final int maximumDepth) {
        this.exceptionConsumer = null;
        this.objectMapper = new ObjectMapper();
        this.maximumDepth = maximumDepth > 0 ? maximumDepth : 1;
    }

    /**
     * Defines a field that will be called throughout the process
     * to verify whether the requested element can be integrated or
     * not to the partial JSON document.
     *
     * @param apFunction The function to execute
     * @return The current instance of {@code Partialize}
     * @since 16.02.13
     */
    public Partialize setAccessPolicy(final Function<AccessPolicy, Boolean> apFunction) {
        this.accessPolicyFunction = apFunction;
        return this;
    }

    /**
     * Defines a callback that will be called throughout the process
     * when exception occurs.
     *
     * @param exceptionCallback The callback to execute
     * @return The current instance of {@code Partialize}
     * @since 16.03.15
     */
    public Partialize setExceptionCallback(final Consumer<Exception> exceptionCallback) {
        this.exceptionConsumer = exceptionCallback;
        return this;
    }

    /**
     * Defines field aliases.
     *
     * @param aliases A {@code Map} defining aliases
     * @return The current instance of {@code Partialize}
     * @since 16.03.10
     */
    public Partialize setAliases(final Map<String, String> aliases) {
        this.aliases = aliases;
        return this;
    }

    /**
     * Build a JSON object from data taken from the scanner and
     * the given class type and instance.
     *
     * @param fields The field query to request
     * @param clazz  The class of the object to render
     * @return An instance of {@code ContainerNode}
     * @see ContainerNode
     * @since 16.01.18
     */
    public ContainerNode buildPartialObject(final String fields, final Class<?> clazz) {
        return this.buildPartialObject(fields, clazz, null);
    }

    /**
     * Build a JSON object from data taken from the scanner and
     * the given class type and instance.
     *
     * @param fields   The field query to request
     * @param clazz    The class of the object to render
     * @param instance The instance of the object to render
     * @return An instance of {@code ContainerNode}
     * @see ContainerNode
     * @since 16.01.18
     */
    public ContainerNode buildPartialObject(final String fields, final Class<?> clazz, final Object instance) {
        if (instance instanceof Collection<?>) {
            final ArrayNode partialArray = this.objectMapper.createArrayNode();
            if (((Collection<?>) instance).size() > 0) {
                for (final Object o : (Collection<?>) instance) {
                    partialArray.add(this.buildPartialObject(-1, fields, o.getClass(), o));
                }
            }
            return partialArray;
        } else {
            return this.buildPartialObject(0, fields, clazz, instance);
        }
    }

    /**
     * Add requested item on the partial JSON document.
     *
     * @param depth        Current depth level
     * @param aliasField   The alias field name
     * @param field        The field name
     * @param args         The field Arguments
     * @param partialArray The current partial JSON document part
     * @param clazz        The class of the object to add
     * @param object       The object to add
     * @since 16.01.18
     */
    private void internalBuild(final int depth, final String aliasField, final String field, final String args,
                               final ArrayNode partialArray, final Class<?> clazz, final Object object) {
        if (depth < this.maximumDepth) {
            if (object == null) {
                partialArray.addNull();
            } else if (object instanceof String) {
                partialArray.add((String) object);
            } else if (object instanceof Integer) {
                partialArray.add((Integer) object);
            } else if (object instanceof Long) {
                partialArray.add((Long) object);
            } else if (object instanceof Double) {
                partialArray.add((Double) object);
            } else if (object instanceof UUID) {
                partialArray.add(object.toString());
            } else if (object instanceof Boolean) {
                partialArray.add((Boolean) object);
            } else if (object instanceof JsonNode) {
                partialArray.addPOJO(object);
            } else if (object instanceof Collection<?>) {
                final ArrayNode anotherPartialArray = partialArray.addArray();
                if (((Collection<?>) object).size() > 0) {
                    for (final Object o : (Collection<?>) object) {
                        this.internalBuild(depth + 1, aliasField, field, args, anotherPartialArray, o.getClass(), o);
                    }
                }
            } else if (object instanceof Enum) {
                final String tmp = object.toString();
                try {
                    partialArray.add(Integer.valueOf(tmp));
                } catch (final NumberFormatException ignore) {
                    partialArray.add(tmp);
                }
            } else {
                final Converter converter = PartializeConverterManager.getInstance().getConverter(object.getClass());
                if (converter != null) {
                    converter.convert(aliasField, object, partialArray);
                } else {
                    partialArray.add(this.buildPartialObject(depth + 1, args, object.getClass(), object));
                }
            }
        }
    }

    /**
     * Add requested item on the partial JSON document.
     *
     * @param depth         Current depth level
     * @param aliasField    The alias field name
     * @param field         The field name
     * @param args          The field Arguments
     * @param partialObject The current partial JSON document part
     * @param clazz         The class of the object to add
     * @param object        The object to add
     * @since 16.01.18
     */
    private void internalBuild(final int depth, final String aliasField, final String field, final String args,
                               final ObjectNode partialObject, final Class<?> clazz, Object object) {
        if (depth <= this.maximumDepth) {
            if (object instanceof Optional) {
                object = ((Optional<?>) object).orElse(null);
            }
            if (object == null) {
                partialObject.putNull(aliasField);
            } else if (object instanceof String) {
                partialObject.put(aliasField, (String) object);
            } else if (object instanceof Integer) {
                partialObject.put(aliasField, (Integer) object);
            } else if (object instanceof Long) {
                partialObject.put(aliasField, (Long) object);
            } else if (object instanceof Double) {
                partialObject.put(aliasField, (Double) object);
            } else if (object instanceof UUID) {
                partialObject.put(aliasField, object.toString());
            } else if (object instanceof Boolean) {
                partialObject.put(aliasField, (Boolean) object);
            } else if (object instanceof JsonNode) {
                partialObject.putPOJO(aliasField, object);
            } else if (object instanceof Collection<?>) {
                final ArrayNode partialArray = partialObject.putArray(aliasField);
                if (((Collection<?>) object).size() > 0) {
                    for (final Object o : (Collection<?>) object) {
                        this.internalBuild(depth, aliasField, field, args, partialArray, o.getClass(), o);
                    }
                }
            } else if (object instanceof Map<?, ?>) {
                this.buildPartialObject(depth + 1, args, object.getClass(), object, partialObject.putObject(aliasField));
            } else if (object instanceof Enum) {
                final String tmp = object.toString();
                try {
                    partialObject.put(aliasField, Integer.valueOf(tmp));
                } catch (final NumberFormatException ignore) {
                    partialObject.put(aliasField, tmp);
                }
            } else {
                final Converter converter = PartializeConverterManager.getInstance().getConverter(object.getClass());
                if (converter != null) {
                    converter.convert(aliasField, object, partialObject);
                } else {
                    this.buildPartialObject(depth + 1, args, object.getClass(), object, partialObject.putObject(aliasField));
                }
            }
        }
    }

    /**
     * Build a JSON object from data taken from the scanner and
     * the given class type and instance.
     *
     * @param depth    The current depth
     * @param fields   The field names to requests
     * @param clazz    The class of the object to render
     * @param instance The instance of the object to render
     * @return A JSON Object
     * @since 16.01.18
     */
    private ContainerNode buildPartialObject(final int depth, final String fields,
                                             final Class<?> clazz, final Object instance) {
        return this.buildPartialObject(depth, fields, clazz, instance, this.objectMapper.createObjectNode());
    }

    /**
     * Build a JSON object from data taken from the scanner and
     * the given class type and instance.
     *
     * @param depth         The current depth
     * @param fields        The field names to requests
     * @param clazz         The class of the object to render
     * @param instance      The instance of the object to render
     * @param partialObject The partial JSON document
     * @return A JSON Object
     * @since 16.01.18
     */
    private ContainerNode buildPartialObject(final int depth, String fields, final Class<?> clazz,
                                             final Object instance, final ObjectNode partialObject) {
        if (depth <= this.maximumDepth) {
            final ObjectType objectType;
            if (clazz.isAnnotationPresent(com.zero_x_baadf00d.partialize.annotation.Partialize.class)) {
                objectType = ObjectType.ANNOTATED;
            } else if (instance instanceof Map<?, ?>) {
                objectType = ObjectType.MAP;
            } else if (instance instanceof Collection<?>) {
                final ArrayNode partialArray = this.objectMapper.createArrayNode();
                if (((Collection<?>) instance).size() > 0) {
                    for (final Object o : (Collection<?>) instance) {
                        this.internalBuild(depth + 1, null, null, null, partialArray, o.getClass(), o);
                    }
                }
                return partialArray;
            } else {
                objectType = ObjectType.NOT_SUPPORTED;
            }

            if (objectType != ObjectType.NOT_SUPPORTED) {
                final List<String> closedFields = new ArrayList<>();

                List<String> allowedFields;
                List<String> defaultFields = null;
                switch (objectType) {
                    case ANNOTATED:
                        allowedFields = Arrays.asList(
                            clazz
                                .getAnnotation(com.zero_x_baadf00d.partialize.annotation.Partialize.class)
                                .allowedFields()
                        );
                        defaultFields = Arrays.asList(
                            clazz
                                .getAnnotation(com.zero_x_baadf00d.partialize.annotation.Partialize.class)
                                .defaultFields()
                        );

                        if (allowedFields.isEmpty()) {
                            allowedFields = new ArrayList<>();
                            for (final Method m : clazz.getDeclaredMethods()) {
                                final String methodName = m.getName();
                                for (final String methodPrefix : Partialize.METHOD_PREFIXES) {
                                    if (methodName.startsWith(methodPrefix)) {
                                        final char[] c = methodName.substring(methodPrefix.length()).toCharArray();
                                        c[0] = Character.toLowerCase(c[0]);
                                        allowedFields.add(new String(c));
                                    }
                                }
                            }
                        }

                        break;
                    case MAP:
                        allowedFields = new ArrayList<>();
                        for (Map.Entry<?, ?> e : ((Map<?, ?>) instance).entrySet()) {
                            allowedFields.add(String.valueOf(e.getKey()));
                        }
                        break;
                    default:
                        throw new NotImplementedException("Can't convert " + clazz.getCanonicalName());
                }

                if (defaultFields == null || defaultFields.isEmpty()) {
                    defaultFields = allowedFields.stream()
                        .map(f -> {
                            if (this.aliases != null && this.aliases.containsValue(f)) {
                                for (Map.Entry<String, String> e : this.aliases.entrySet()) {
                                    if (e.getValue().compareToIgnoreCase(f) == 0) {
                                        return e.getKey();
                                    }
                                }
                            }
                            return f;
                        })
                        .collect(Collectors.toList());
                }
                if (fields == null || fields.length() == 0) {
                    fields = defaultFields.stream().collect(Collectors.joining(","));
                }
                Scanner scanner = new Scanner(fields);
                scanner.useDelimiter(com.zero_x_baadf00d.partialize.Partialize.SCANNER_DELIMITER);
                while (scanner.hasNext()) {
                    String word = scanner.next();
                    String args = null;
                    if (word.compareTo("*") == 0) {
                        final StringBuilder sb = new StringBuilder();
                        if (scanner.hasNext()) {
                            scanner.useDelimiter("\n");
                            sb.append(",");
                            sb.append(scanner.next());
                        }
                        final Scanner newScanner = new Scanner(allowedFields.stream()
                            .filter(f -> !closedFields.contains(f))
                            .map(f -> {
                                if (this.aliases != null && this.aliases.containsValue(f)) {
                                    for (Map.Entry<String, String> e : this.aliases.entrySet()) {
                                        if (e.getValue().compareToIgnoreCase(f) == 0) {
                                            return e.getKey();
                                        }
                                    }
                                }
                                return f;
                            })
                            .collect(Collectors.joining(",")) + sb.toString());
                        newScanner.useDelimiter(com.zero_x_baadf00d.partialize.Partialize.SCANNER_DELIMITER);
                        scanner.close();
                        scanner = newScanner;
                    }
                    if (word.contains("(")) {
                        while (scanner.hasNext() && (StringUtils.countMatches(word, "(") != StringUtils.countMatches(word, ")"))) {
                            word += "," + scanner.next();
                        }
                        final Matcher m = this.fieldArgsPattern.matcher(word);
                        if (m.find()) {
                            word = m.group(1);
                            args = m.group(2);
                        }
                    }
                    final String aliasField = word;
                    final String field = this.aliases != null && this.aliases.containsKey(aliasField) ? this.aliases.get(aliasField) : aliasField;
                    if (allowedFields.stream().anyMatch(f -> f.toLowerCase(Locale.ENGLISH).compareTo(field.toLowerCase(Locale.ENGLISH)) == 0)) {
                        if (this.accessPolicyFunction != null && !this.accessPolicyFunction.apply(new AccessPolicy(clazz, instance, field))) {
                            continue;
                        }
                        closedFields.add(aliasField);
                        switch (objectType) {
                            case ANNOTATED:
                                for (final String methodPrefix : Partialize.METHOD_PREFIXES) {
                                    try {
                                        final Method method = clazz.getMethod(methodPrefix + WordUtils.capitalize(field));
                                        final Object object = method.invoke(instance);
                                        this.internalBuild(depth, aliasField, field, args, partialObject, clazz, object);
                                        break;
                                    } catch (final IllegalAccessException | InvocationTargetException | NoSuchMethodException | NullPointerException ignore) {
                                        try {
                                            final Method method = clazz.getMethod(field);
                                            final Object object = method.invoke(instance);
                                            this.internalBuild(depth, aliasField, field, args, partialObject, clazz, object);
                                            break;
                                        } catch (final IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {
                                            if (this.exceptionConsumer != null) {
                                                this.exceptionConsumer.accept(ex);
                                            }
                                        }
                                    }
                                }
                                break;
                            case MAP:
                                final Map<?, ?> tmpMap = (Map<?, ?>) instance;
                                if (tmpMap.containsKey(field)) {
                                    final Object object = tmpMap.get(field);
                                    this.internalBuild(depth, aliasField, field, args, partialObject, clazz, object);
                                }
                                break;
                            default:
                                break;
                        }
                    }
                }
            } else {
                throw new RuntimeException("Can't convert " + clazz.getCanonicalName());
            }
        }
        return partialObject;
    }
}
