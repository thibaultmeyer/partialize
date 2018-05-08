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
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * BasicTest.
 *
 * @author Thibault Meyer
 * @version 17.06.28
 * @since 16.01.18
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BasicTest {

    /**
     * Handle to the object used on these tests.
     *
     * @since 16.01.18
     */
    private AccountPojo account;

    /**
     * Initialize data.
     *
     * @since 16.01.18
     */
    @Before
    public void initializePojo() {
        if (this.account == null) {
            this.account = new AccountPojo();
            this.account.setId(1);
            this.account.setUid(UUID.randomUUID());
            this.account.setFirstName("John");
            this.account.setLastName("Smith");

            final List<EmailPojo> emails = new ArrayList<>();
            this.account.setEmails(emails);

            final EmailAttributePojo attributePojo_1 = new EmailAttributePojo();
            attributePojo_1.setKey("key_1");
            attributePojo_1.setValue("hello");
            final EmailAttributePojo attributePojo_2 = new EmailAttributePojo();
            attributePojo_2.setKey("key_2");
            attributePojo_2.setValue("world");

            EmailPojo email = new EmailPojo();
            email.setId(1);
            email.setUid(UUID.randomUUID());
            email.setEmail("john.smith@domain.local");
            email.setDefault(true);
            email.setDead(false);
            email.setAttributes(Arrays.asList(attributePojo_1, attributePojo_2));
            emails.add(email);

            email = new EmailPojo();
            email.setId(2);
            email.setUid(UUID.randomUUID());
            email.setEmail("john@domain.local");
            email.setDefault(false);
            emails.add(email);
        }
    }

    /**
     * @since 16.01.18
     */
    @Test
    public void basicTest001() {
        final String fields = "firstName,lastName,emails(uid)";
        final com.zero_x_baadf00d.partialize.Partialize partialize = new com.zero_x_baadf00d.partialize.Partialize();
        final ContainerNode result = partialize.buildPartialObject(fields, AccountPojo.class, this.account);

        Assert.assertNotNull(result);
        Assert.assertEquals("John", result.get("firstName").asText());
        Assert.assertEquals("Smith", result.get("lastName").asText());
        Assert.assertFalse(result.get("emails").get(0).has("email"));
        Assert.assertTrue(result.get("emails").has(1));
        Assert.assertFalse(result.get("emails").has(2));
        Assert.assertEquals(this.account.getEmails().get(0).getUid().toString(), result.get("emails").get(0).get("uid").asText());
    }

    /**
     * @since 16.01.18
     */
    @Test
    public void basicTest002() {
        final String fields = "*";
        final com.zero_x_baadf00d.partialize.Partialize partialize = new com.zero_x_baadf00d.partialize.Partialize();
        final ContainerNode result = partialize.buildPartialObject(fields, AccountPojo.class, this.account);
        Assert.assertNotNull(result);
        Assert.assertTrue(result.has("uid"));
        Assert.assertFalse(result.has("id"));
        Assert.assertTrue(result.has("emails"));
        Assert.assertEquals("John", result.get("firstName").asText());
        Assert.assertEquals("Smith", result.get("lastName").asText());
    }

    /**
     * @since 16.01.18
     */
    @Test
    public void basicTest003() {
        final String fields = "emails(*),firstName";
        final com.zero_x_baadf00d.partialize.Partialize partialize = new com.zero_x_baadf00d.partialize.Partialize();
        final ContainerNode result = partialize.buildPartialObject(fields, AccountPojo.class, this.account);

        Assert.assertNotNull(result);
        Assert.assertEquals("John", result.get("firstName").asText());
        Assert.assertTrue(result.get("emails").has(0));
        Assert.assertTrue(result.get("emails").has(1));
        Assert.assertFalse(result.get("emails").has(2));
        Assert.assertTrue(result.get("emails").get(0).has("email"));
        Assert.assertTrue(result.get("emails").get(0).has("uid"));
        Assert.assertFalse(result.get("emails").get(0).has("id"));
        Assert.assertEquals(this.account.getEmails().get(0).getUid().toString(), result.get("emails").get(0).get("uid").asText());
    }

    /**
     * @since 16.01.18
     */
    @Test
    public void basicTest004() {
        final String fields = "*,emails(uid,email)";
        final com.zero_x_baadf00d.partialize.Partialize partialize = new com.zero_x_baadf00d.partialize.Partialize();
        final ContainerNode result = partialize.buildPartialObject(fields, AccountPojo.class, this.account);

        Assert.assertNotNull(result);
        Assert.assertEquals("John", result.get("firstName").asText());
        Assert.assertEquals("Smith", result.get("lastName").asText());
        Assert.assertTrue(result.get("emails").has(0));
        Assert.assertTrue(result.get("emails").has(1));
        Assert.assertFalse(result.get("emails").has(2));
        Assert.assertTrue(result.get("emails").get(0).has("email"));
        Assert.assertTrue(result.get("emails").get(0).has("uid"));
        Assert.assertFalse(result.get("emails").get(0).has("id"));
        Assert.assertEquals(this.account.getEmails().get(0).getUid().toString(), result.get("emails").get(0).get("uid").asText());
    }

    /**
     * @since 16.09.27
     */
    @Test
    public void basicTest005() {
        this.account.setEmails(null);
        final String fields = "emails";
        final com.zero_x_baadf00d.partialize.Partialize partialize = new com.zero_x_baadf00d.partialize.Partialize();
        final ContainerNode result = partialize.buildPartialObject(fields, AccountPojo.class, this.account);

        Assert.assertNotNull(result);
        Assert.assertTrue(result.has("emails"));
        Assert.assertTrue(result.get("emails").isNull());
    }

    /**
     * @since 17.06.28
     */
    @Test
    public void basicTest006() {
        final String fields = "*";
        final com.zero_x_baadf00d.partialize.Partialize partialize = new com.zero_x_baadf00d.partialize.Partialize();
        final ContainerNode result = partialize.buildPartialObject(fields, EmailPojo.class, this.account.emails.get(0));
        Assert.assertNotNull(result);
        Assert.assertTrue(result.has("dead"));
        Assert.assertFalse(result.get("dead").isNull());
        Assert.assertTrue(result.get("dead").isBoolean());
        Assert.assertFalse(result.get("dead").asBoolean());
    }

    /**
     * EmailAttributePojo.
     *
     * @author Thibault Meyer
     * @version 17.06.28
     * @since 17.06.28
     */
    @Partialize(
        allowedFields = {"key", "value"}
    )
    public static class EmailAttributePojo {
        private String key;
        private String value;

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

    /**
     * EmailPojo.
     *
     * @author Thibault Meyer
     * @version 17.06.28
     * @since 16.01.18
     */
    @Partialize(
        allowedFields = {"uid", "email", "isDefault", "dead", "attributes"},
        defaultFields = "uid"
    )
    public static class EmailPojo {

        private int id;
        private UUID uid;
        private String email;
        private boolean isDefault;
        private boolean dead;
        private List<EmailAttributePojo> attributes;

        public int getId() {
            return this.id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public UUID getUid() {
            return this.uid;
        }

        public void setUid(UUID uid) {
            this.uid = uid;
        }

        public String getEmail() {
            return this.email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public boolean isDefault() {
            return this.isDefault;
        }

        public void setDefault(boolean aDefault) {
            this.isDefault = aDefault;
        }

        public boolean isDead() {
            return this.dead;
        }

        public void setDead(boolean dead) {
            this.dead = dead;
        }

        public boolean hasAttributes() {
            return attributes != null && !attributes.isEmpty();
        }

        public List<EmailAttributePojo> getAttributes() {
            return attributes;
        }

        public void setAttributes(List<EmailAttributePojo> attributes) {
            this.attributes = attributes;
        }
    }

    /**
     * AccountPojo.
     *
     * @author Thibault Meyer
     * @version 16.01.18
     * @since 16.01.18
     */
    @Partialize(
        allowedFields = {"uid", "firstName", "lastName", "emails"},
        defaultFields = {"uid", "firstName", "lastName"}
    )
    public static class AccountPojo {

        private int id;
        private UUID uid;
        private String firstName;
        private String lastName;
        private List<EmailPojo> emails;

        public int getId() {
            return this.id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public UUID getUid() {
            return this.uid;
        }

        public void setUid(UUID uid) {
            this.uid = uid;
        }

        public String getFirstName() {
            return this.firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return this.lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public List<EmailPojo> getEmails() {
            return this.emails;
        }

        public void setEmails(List<EmailPojo> emails) {
            this.emails = emails;
        }
    }
}
