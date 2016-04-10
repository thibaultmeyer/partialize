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

import com.fasterxml.jackson.databind.node.ContainerNode;
import com.zero_x_baadf00d.partialize.annotation.Partialize;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.HashMap;

/**
 * AliasTest.
 *
 * @author Thibault Meyer
 * @version 16.03.16
 * @since 16.03.10
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AliasTest {

    /**
     * @since 16.03.10
     */
    @Test
    public void aliasTest001() {
        final AliasPojo aliasPojo = new AliasPojo();
        final String fields = "number,myNumber,secret,pojo(*)";
        final com.zero_x_baadf00d.partialize.Partialize partialize = new com.zero_x_baadf00d.partialize.Partialize();
        partialize.setAliases(new HashMap<String, String>() {{
            put("myNumber", "number");
            put("secret", "number");
            put("logo", "logoUrl");
            put("cover", "coverUrl");
        }});
        final ContainerNode result = partialize.buildPartialObject(fields, AliasPojo.class, aliasPojo);

        Assert.assertNotNull(result);
        Assert.assertNotNull(result.get("number"));
        Assert.assertEquals(42, result.get("number").asInt());
        Assert.assertNotNull(result.get("myNumber"));
        Assert.assertEquals(42, result.get("myNumber").asInt());
        Assert.assertNotNull(result.get("secret"));
        Assert.assertEquals(42, result.get("secret").asInt());
        Assert.assertNotNull(result.get("pojo"));
        Assert.assertNotNull(result.get("pojo").get("cover"));
    }

    /**
     * @since 16.03.16
     */
    @Test
    public void aliasTest002() {
        final AliasPojo aliasPojo = new AliasPojo();
        final String fields = "*";
        final com.zero_x_baadf00d.partialize.Partialize partialize = new com.zero_x_baadf00d.partialize.Partialize();
        partialize.setAliases(new HashMap<String, String>() {{
            put("myNumber", "number");
            put("cover", "coverUrl");
        }});
        final ContainerNode result = partialize.buildPartialObject(fields, AliasPojo.class, aliasPojo);

        Assert.assertNotNull(result);
        Assert.assertNotNull(result.get("myNumber"));
        Assert.assertNotNull(result.get("pojo"));
        Assert.assertNotNull(result.get("pojo").get("cover"));
    }

    /**
     * MixedPojo.
     *
     * @author Thibault Meyer
     * @version 16.03.16
     * @since 16.03.10
     */
    @Partialize(allowedFields = {"number", "pojo"})
    public static class AliasPojo {

        private final int number;

        private final AliasPojo2 aliasPojo2;

        public AliasPojo() {
            this.number = 42;
            this.aliasPojo2 = new AliasPojo2();
        }

        public int getNumber() {
            return this.number;
        }

        public int getSecret() {
            return 23489;
        }

        public AliasPojo2 getPojo() {
            return this.aliasPojo2;
        }
    }

    /**
     * MixedPojo2.
     *
     * @author Thibault Meyer
     * @version 16.03.16
     * @since 16.03.16
     */
    @Partialize(allowedFields = {"coverUrl"})
    public static class AliasPojo2 {

        public String getCoverUrl() {
            return "http://domain.local/cover.png";
        }
    }
}
