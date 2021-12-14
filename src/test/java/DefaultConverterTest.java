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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.zero_x_baadf00d.partialize.converter.Converter;
import com.zero_x_baadf00d.partialize.converter.DefaultConverter;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

/**
 * DefaultConverterTest.
 *
 * @author Thibault Meyer
 * @version 20.12.14
 * @since 16.10.04
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DefaultConverterTest {

    /**
     * @since 16.10.04
     */
    @Test
    public void defaultConverter001() {
        final Converter<Object> converter = new DefaultConverter();
        final ObjectNode objectNode = new ObjectMapper().createObjectNode();
        final Pojo pojo = new Pojo();

        converter.convert("data", pojo, objectNode);

        Assert.assertTrue(objectNode.has("data"));
        Assert.assertEquals(pojo.toString(), objectNode.get("data").asText());
    }

    /**
     * @since 16.10.04
     */
    @Test
    public void defaultConverter002() {
        final Converter<Object> converter = new DefaultConverter();
        final ArrayNode arrayNode = new ObjectMapper().createArrayNode();
        final Pojo pojo = new Pojo();

        converter.convert("data", pojo, arrayNode);

        Assert.assertEquals(1, arrayNode.size());
        Assert.assertEquals(pojo.toString(), arrayNode.get(0).asText());
    }

    /**
     * Pojo.
     *
     * @author Thibault Meyer
     * @version 16.10.04
     * @since 16.10.04
     */
    public static class Pojo {

        @Override
        public String toString() {
            return "pojo";
        }
    }
}
