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
import com.zero_x_baadf00d.partialize.annotation.Partialize;
import converters.JodaDateTimeConverter;
import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

/**
 * BasicTest.
 *
 * @author Thibault Meyer
 * @version 16.03.22
 * @since 16.03.22
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class InheritanceTest {

    /**
     * @since 16.03.22
     */
    @Test
    public void inheritanceTest001() {
        final Pojo pojo = new Pojo();
        final String fields = "id,name,createdAt";
        final com.zero_x_baadf00d.partialize.Partialize partialize = new com.zero_x_baadf00d.partialize.Partialize();
        com.zero_x_baadf00d.partialize.PartializeConverterManager.getInstance().registerConverter(new JodaDateTimeConverter());
        final ContainerNode result = partialize.buildPartialObject(fields, Pojo.class, pojo);

        Assert.assertNotNull(result);
        Assert.assertEquals(42, result.get("id").asInt());
        Assert.assertEquals("John", result.get("name").asText());
        Assert.assertEquals(true, result.has("createdAt"));
    }

    /**
     * BasePojo.
     *
     * @author Thibault Meyer
     * @version 16.03.22
     * @since 16.03.22
     */
    public class BasePojo {

        private int id;

        private DateTime createdAt;

        protected BasePojo() {
            this.createdAt = DateTime.now();
            this.id = 42;
        }

        public int getId() {
            return this.id;
        }

        public DateTime getCreatedAt() {
            return this.createdAt;
        }
    }

    /**
     * Pojo.
     *
     * @author Thibault Meyer
     * @version 16.03.22
     * @since 16.03.22
     */
    @Partialize(allowedFields = {"id", "createdAt", "name"})
    public class Pojo extends BasePojo {

        private String name;

        public Pojo() {
            super();
            this.name = "John";
        }

        public String getName() {
            return this.name;
        }
    }
}
