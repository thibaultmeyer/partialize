/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 - 2017 Thibault Meyer
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

import com.fasterxml.jackson.databind.node.ContainerNode;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.HashMap;
import java.util.Map;

/**
 * MapTest.
 *
 * @author Thibault Meyer
 * @version 16.12.05
 * @since 16.12.05
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MapTest {

    /**
     * @since 16.12.05
     */
    private Map<String, Object> simpleMap;

    /**
     * @since 16.12.05
     */
    private Map<String, Object> complexMap;

    /**
     * Initialize data.
     *
     * @since 16.12.05
     */
    @Before
    public void initializePojo() {
        this.simpleMap = new HashMap<String, Object>() {{
            put("string", "hello world!");
            put("boolean", true);
            put("number", 45.63);
            put("null", null);
        }};

        this.complexMap = new HashMap<String, Object>() {{
            put("objA", new HashMap<String, Object>() {{
                put("name", "demo.bin");
                put("size", 132456789);
                put("isEnabled", true);
            }});
            put("objB", null);
            put("objC", new HashMap<String, Object>() {{
                put("subObject", new HashMap<String, Object>() {{
                    put("key", "value");
                    put("key2", "bye bye");
                }});
            }});
        }};
    }

    /**
     * @since 16.12.05
     */
    @Test
    public void mapTest001() {
        final String fields = "string,boolean,null";
        final com.zero_x_baadf00d.partialize.Partialize partialize = new com.zero_x_baadf00d.partialize.Partialize();
        final ContainerNode result = partialize.buildPartialObject(fields, Map.class, this.simpleMap);
        Assert.assertNotNull(result);
        Assert.assertEquals("hello world!", result.get("string").asText());
        Assert.assertEquals(true, result.get("boolean").asBoolean());
        Assert.assertEquals(true, result.get("null").isNull());
    }

    /**
     * @since 16.12.05
     */
    @Test
    public void mapTest002() {
        final String fields = "*";
        final com.zero_x_baadf00d.partialize.Partialize partialize = new com.zero_x_baadf00d.partialize.Partialize();
        final ContainerNode result = partialize.buildPartialObject(fields, Map.class, this.simpleMap);
        Assert.assertNotNull(result);
        Assert.assertEquals("hello world!", result.get("string").asText());
        Assert.assertEquals(true, result.get("boolean").asBoolean());
        Assert.assertEquals(45.63, result.get("number").asDouble(), 2);
        Assert.assertEquals(true, result.get("null").isNull());
    }

    /**
     * @since 16.12.05
     */
    @Test
    public void mapTest003() {
        final String fields = "objA,objB";
        final com.zero_x_baadf00d.partialize.Partialize partialize = new com.zero_x_baadf00d.partialize.Partialize();
        final ContainerNode result = partialize.buildPartialObject(fields, Map.class, this.complexMap);
        Assert.assertNotNull(result);
        Assert.assertEquals(true, result.get("objA").isObject());
        Assert.assertEquals("demo.bin", result.get("objA").get("name").asText());
        Assert.assertEquals(132456789, result.get("objA").get("size").asInt());
        Assert.assertEquals(true, result.get("objA").get("isEnabled").asBoolean());
        Assert.assertEquals(true, result.get("objA").get("isEnabled").asBoolean());
        Assert.assertEquals(true, result.get("objB").isNull());
    }

    /**
     * @since 16.12.05
     */
    @Test
    public void mapTest004() {
        final String fields = "objA(name,size)";
        final com.zero_x_baadf00d.partialize.Partialize partialize = new com.zero_x_baadf00d.partialize.Partialize();
        final ContainerNode result = partialize.buildPartialObject(fields, Map.class, this.complexMap);
        Assert.assertNotNull(result);
        Assert.assertEquals(true, result.get("objA").isObject());
        Assert.assertEquals("demo.bin", result.get("objA").get("name").asText());
        Assert.assertEquals(132456789, result.get("objA").get("size").asInt());
    }

    /**
     * @since 16.12.05
     */
    @Test
    public void mapTest005() {
        final String fields = "objC(subObject(key))";
        final com.zero_x_baadf00d.partialize.Partialize partialize = new com.zero_x_baadf00d.partialize.Partialize();
        final ContainerNode result = partialize.buildPartialObject(fields, Map.class, this.complexMap);
        Assert.assertNotNull(result);
        Assert.assertEquals(true, result.has("objC"));
        Assert.assertEquals(true, result.get("objC").isObject());
        Assert.assertEquals(true, result.get("objC").has("subObject"));
        Assert.assertEquals(true, result.get("objC").get("subObject").isObject());
        Assert.assertEquals(true, result.get("objC").get("subObject").has("key"));
        Assert.assertEquals("value", result.get("objC").get("subObject").get("key").asText());
    }
}
