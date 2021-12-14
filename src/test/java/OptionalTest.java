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
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.List;
import java.util.Optional;

/**
 * OptionalTest.
 *
 * @author Thibault Meyer
 * @version 20.12.14
 * @since 18.05.08
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class OptionalTest {

    private PojoWithOptional pojoWithOptional;

    /**
     * Initialize data.
     *
     * @since 18.05.08
     */
    @Before
    public void initializePojo() {
        if (this.pojoWithOptional == null) {
            this.pojoWithOptional = new PojoWithOptional();
            this.pojoWithOptional.setFirstName("John");
            this.pojoWithOptional.setAge(18);
        }
    }

    /**
     * @since 18.05.08
     */
    @Test
    public void optionalTest001() {
        final com.zero_x_baadf00d.partialize.Partialize partialize = new com.zero_x_baadf00d.partialize.Partialize();

        final JsonNode result = partialize.buildPartialObject(
            "firstName,age,tags",
            OptionalTest.PojoWithOptional.class,
            this.pojoWithOptional
        );

        Assert.assertNotNull(result);
        Assert.assertEquals("John", result.get("firstName").asText());
        Assert.assertEquals(18, result.get("age").asInt());
        Assert.assertTrue(result.get("tags").isNull());
    }

    /**
     * PojoWithOptional.
     *
     * @author Thibault Meyer
     * @version 18.05.08
     * @since 18.05.08
     */
    @Partialize(allowedFields = {"firstName", "age", "tags"})
    public static class PojoWithOptional {

        private String firstName;
        private Integer age;
        private List<String> tags;

        public String getFirstName() {
            return this.firstName;
        }

        public void setFirstName(final String firstName) {
            this.firstName = firstName;
        }

        public Optional<Integer> getAge() {
            return Optional.ofNullable(this.age);
        }

        public void setAge(final Integer age) {
            this.age = age;
        }

        public Optional<List<String>> getTags() {
            return Optional.ofNullable(this.tags);
        }

        public void setTags(final List<String> tags) {
            this.tags = tags;
        }
    }
}
