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
import com.zero_x_baadf00d.partialize.annotation.Partialize;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

/**
 * StaticTest.
 *
 * @author Thibault Meyer
 * @version 20.12.14
 * @since 16.01.18
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class StaticTest {

    /**
     * @since 16.01.18
     */
    @Test
    public void staticTest001() {
        final String fields = "buildDate";
        final com.zero_x_baadf00d.partialize.Partialize partialize = new com.zero_x_baadf00d.partialize.Partialize();

        final JsonNode result = partialize.buildPartialObject(fields, StaticPojo.class);

        Assert.assertNotNull(result);
        Assert.assertTrue(result.has("buildDate"));
        Assert.assertEquals("2016-01-10", result.get("buildDate").asText());
    }

    /**
     * @since 16.01.18
     */
    @Test
    public void staticTest002() {
        final MixedPojo mixedPojo = new MixedPojo();
        final String fields = "number,42,null";
        final com.zero_x_baadf00d.partialize.Partialize partialize = new com.zero_x_baadf00d.partialize.Partialize();

        final JsonNode result = partialize.buildPartialObject(fields, MixedPojo.class, mixedPojo);

        Assert.assertNotNull(result);
        Assert.assertTrue(result.has("number"));
        Assert.assertTrue(result.has("42"));
        Assert.assertEquals(42, result.get("number").asInt());
        Assert.assertEquals(42, result.get("42").asInt());
        Assert.assertTrue(result.get("null").isNull());
    }

    /**
     * StaticPojo.
     *
     * @author Thibault Meyer
     * @version 16.01.18
     * @since 16.01.18
     */
    @Partialize(allowedFields = "buildDate")
    public static class StaticPojo {

        public static String getBuildDate() {
            return "2016-01-10";
        }
    }

    /**
     * MixedPojo.
     *
     * @author Thibault Meyer
     * @version 16.01.18
     * @since 16.01.18
     */
    @Partialize(allowedFields = {"number", "42", "null"})
    public static class MixedPojo {

        private final int number;

        public MixedPojo() {
            this.number = 42;
        }

        public static int get42() {
            return 42;
        }

        public int getNumber() {
            return this.number;
        }

        public String getNull() {
            return null;
        }
    }
}
