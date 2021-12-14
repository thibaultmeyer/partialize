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

import com.fasterxml.jackson.databind.JsonNode;
import com.zero_x_baadf00d.partialize.Partialize;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * DepthTest.
 *
 * @author Thibault Meyer
 * @version 20.12.14
 * @since 16.12.06
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DepthTest {

    /**
     * @since 16.12.05
     */
    private List<Object> list;

    /**
     * @since 16.12.05
     */
    private Map<String, Object> map;

    /**
     * Initialize data.
     *
     * @since 16.12.05
     */
    @Before
    public void initializePojo() {
        this.list = new ArrayList<>() {{
            add(new ArrayList<>() {{
                add(new ArrayList<>() {{
                    add(new ArrayList<>() {{
                        add("Hello");
                    }});
                }});
            }});
        }};
        this.map = new HashMap<>() {{
            put("obj", new HashMap<String, Object>() {{
                put("obj", new HashMap<String, Object>() {{
                    put("obj", "Hello");
                }});
            }});
            put("obj2", new HashMap<String, Object>() {{
                put("obj2", new ArrayList<>() {{
                    add(new HashMap<String, Object>() {{
                        put("obj2", new ArrayList<>() {{
                            add("Hello");
                        }});
                    }});
                }});
            }});
        }};
    }

    @Test
    public void depthTest001() {
        final Partialize partialize = new Partialize(1);

        final JsonNode result = partialize.buildPartialObject(null, List.class, this.list);

        Assert.assertNotNull(result);
        Assert.assertTrue(result.has(0));
        Assert.assertTrue(result.get(0).isArray());
        Assert.assertTrue(result.get(0).has(0));
        Assert.assertFalse(result.get(0).get(0).has(0));
    }

    @Test
    public void depthTest002() {
        final Partialize partialize = new Partialize(1);

        final JsonNode result = partialize.buildPartialObject(null, List.class, this.map);

        Assert.assertNotNull(result);
        Assert.assertTrue(result.has("obj"));
        Assert.assertTrue(result.get("obj").isObject());
        Assert.assertTrue(result.get("obj").has("obj"));
        Assert.assertEquals(0, result.get("obj").get("obj").size());
        Assert.assertTrue(result.has("obj2"));
        Assert.assertTrue(result.get("obj2").isObject());
        Assert.assertTrue(result.get("obj2").has("obj2"));
        Assert.assertEquals(0, result.get("obj2").get("obj2").size());
    }
}
