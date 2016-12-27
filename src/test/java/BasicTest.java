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
import java.util.List;
import java.util.UUID;

/**
 * BasicTest.
 *
 * @author Thibault Meyer
 * @version 16.10.04
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

            EmailPojo email = new EmailPojo();
            email.setId(1);
            email.setUid(UUID.randomUUID());
            email.setEmail("john.smith@domain.local");
            email.setDefault(true);
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
     * EmailPojo.
     *
     * @author Thibault Meyer
     * @version 16.01.18
     * @since 16.01.18
     */
    @Partialize(
        allowedFields = {"uid", "email", "isDefault"},
        defaultFields = "uid"
    )
    public static class EmailPojo {

        private int id;
        private UUID uid;
        private String email;
        private boolean isDefault;

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
