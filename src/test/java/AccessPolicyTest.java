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
import com.zero_x_baadf00d.partialize.policy.AccessPolicy;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * AccessPolicyTest.
 *
 * @author Thibault Meyer
 * @version 16.10.04
 * @since 16.10.04
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AccessPolicyTest {

    /**
     * @since 16.10.04
     */
    @Test
    public void accessPolicy001() {
        final String fields = "firstName,password";
        final com.zero_x_baadf00d.partialize.Partialize partialize = new com.zero_x_baadf00d.partialize.Partialize();
        partialize.setAccessPolicy(new Function<AccessPolicy, Boolean>() {

            private final List<String> granted = new ArrayList<String>() {{
                add("firstName");
            }};

            @Override
            public Boolean apply(AccessPolicy accessPolicy) {
                return granted.contains(accessPolicy.field);
            }
        });
        final ContainerNode result = partialize.buildPartialObject(fields, AccessPolicyTest.Pojo.class, new Pojo());

        Assert.assertNotNull(result);
        Assert.assertTrue(result.has("firstName"));
        Assert.assertFalse(result.has("password"));
    }

    /**
     * Pojo.
     *
     * @author Thibault Meyer
     * @version 16.10.04
     * @since 16.10.04
     */
    @Partialize(allowedFields = {"firstName", "password"})
    public static class Pojo {

        private String firstName;
        private String password;

        public Pojo() {
            this.firstName = "John";
            this.password = "132456";
        }

        public String getFirstName() {
            return this.firstName;
        }

        public String getPassword() {
            return this.password;
        }
    }
}
