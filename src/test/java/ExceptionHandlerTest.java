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

import java.util.concurrent.atomic.AtomicReference;

/**
 * ExceptionHandlerTest.
 *
 * @author Thibault Meyer
 * @version 22.01.29
 * @since 22.01.29
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ExceptionHandlerTest {

    /**
     * @since 22.01.29
     */
    @Test
    public void exceptionHandler001() {
        final String fields = "firstName,password";
        final com.zero_x_baadf00d.partialize.Partialize partialize = new com.zero_x_baadf00d.partialize.Partialize();
        final AtomicReference<String> atomicString = new AtomicReference<>(null);
        partialize.setExceptionCallback(exception -> atomicString.set(exception.getMessage()));

        final JsonNode result = partialize.buildPartialObject(fields, Pojo.class, new Pojo());

        Assert.assertNotNull(result);

        // Because "getPassword" is private
        Assert.assertNotNull(atomicString.get());
    }

    /**
     * @since 22.01.29
     */
    @Test
    public void exceptionHandler002() {
        final String fields = "firstName,subPojo(content)";
        final com.zero_x_baadf00d.partialize.Partialize partialize = new com.zero_x_baadf00d.partialize.Partialize();
        final AtomicReference<String> atomicString = new AtomicReference<>(null);
        partialize.setExceptionCallback(exception -> atomicString.set(exception.getMessage()));

        final JsonNode result = partialize.buildPartialObject(fields, Pojo.class, new Pojo());

        Assert.assertNotNull(result);

        // Because "SubPojo" is not annotated
        Assert.assertNotNull(atomicString.get());
    }

    /**
     * @since 22.01.29
     */
    @Test(expected = RuntimeException.class)
    public void exceptionHandler003() {
        final String fields = "firstName,subPojo(content)";
        final com.zero_x_baadf00d.partialize.Partialize partialize = new com.zero_x_baadf00d.partialize.Partialize();

        partialize.buildPartialObject(fields, Pojo.class, new Pojo());
    }

    /**
     * Pojo.
     *
     * @author Thibault Meyer
     * @version 20.12.14
     * @since 16.10.04
     */
    @Partialize(allowedFields = {"firstName", "password", "subPojo"})
    public static class Pojo {

        private final String firstName;
        private final String password;
        private final SubPojo subPojo;

        public Pojo() {
            this.firstName = "John";
            this.password = "132456";
            this.subPojo = new SubPojo();
        }

        public String getFirstName() {
            return this.firstName;
        }

        public SubPojo getSubPojo() {
            return subPojo;
        }

        private String getPassword() {
            return this.password;
        }
    }

    public static class SubPojo {

        private final String content;

        public SubPojo() {
            this.content = "content";
        }

        public String getContent() {
            return content;
        }
    }
}
