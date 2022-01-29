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
import converters.JodaDateTimeConverter;
import org.joda.time.DateTime;
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
 * StandardTest.
 *
 * @author Thibault Meyer
 * @version 20.12.14
 * @since 16.01.18
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class StandardTest {

    /**
     * Handle to the object used on these tests.
     *
     * @since 16.01.18
     */
    private BankAccountPojo bankAccount;

    /**
     * Current datetime.
     *
     * @since 16.01.18
     */
    private DateTime currentDateTime;

    /**
     * Initialize data.
     *
     * @since 16.01.18
     */
    @Before
    public void initializePojo() {
        if (this.bankAccount == null) {
            com.zero_x_baadf00d.partialize.PartializeConverterManager.getInstance().registerConverter(new JodaDateTimeConverter());

            this.currentDateTime = DateTime.now();
            this.bankAccount = new BankAccountPojo();
            this.bankAccount.setAmount(42.84);
            this.bankAccount.setDisplayName("My bank account");
            this.bankAccount.setCreateDate(this.currentDateTime);
            final List<List<String>> listOfList = new ArrayList<>();
            final List<String> item = new ArrayList<>();
            item.add("HELLO");
            item.add("WORLD");
            listOfList.add(item);
            listOfList.add(item);
            this.bankAccount.setListOfList(listOfList);
            final Map<String, Object> attributes = new HashMap<>();
            attributes.put("null", null);
            attributes.put("integer", 42);
            attributes.put("double", 42.84);
            attributes.put("boolean", false);
            attributes.put("string", "hello world");
            this.bankAccount.setAttributes(attributes);
            this.bankAccount.setLevel(BankAccountLevel.STANDARD);
            this.bankAccount.setNetwork(BankAccountNetwork.NET_1);
        }
    }

    /**
     * @since 16.01.18
     */
    @Test
    public void standardTest001() {
        final String fields = "displayName,amount,createDate,level,network";
        final com.zero_x_baadf00d.partialize.Partialize partialize = new com.zero_x_baadf00d.partialize.Partialize();

        final JsonNode result = partialize.buildPartialObject(fields, BankAccountPojo.class, this.bankAccount);

        Assert.assertNotNull(result);
        Assert.assertEquals("My bank account", result.get("displayName").asText());
        Assert.assertEquals(42.84, result.get("amount").asDouble(), 0);
        Assert.assertNotNull(result.get("createDate"));
        Assert.assertEquals(this.currentDateTime.toString("yyyy-MM-dd'T'HH:mm:ss"), result.get("createDate").asText());
        Assert.assertEquals(BankAccountLevel.STANDARD.toString(), result.get("level").asText());
        Assert.assertEquals(BankAccountNetwork.NET_1.toString(), result.get("network").asText());
    }

    /**
     * @since 16.01.18
     */
    @Test
    public void standardTest002() {
        final String fields = "displayName,listOfList,createDate";
        final com.zero_x_baadf00d.partialize.Partialize partialize = new com.zero_x_baadf00d.partialize.Partialize();

        final JsonNode result = partialize.buildPartialObject(fields, BankAccountPojo.class, this.bankAccount);

        Assert.assertNotNull(result);
        Assert.assertEquals("My bank account", result.get("displayName").asText());
        Assert.assertNotNull(result.get("createDate"));
        Assert.assertEquals(this.currentDateTime.toString("yyyy-MM-dd'T'HH:mm:ss"), result.get("createDate").asText());
        Assert.assertTrue(result.has("listOfList"));
        Assert.assertTrue(result.get("listOfList").has(0));
        Assert.assertTrue(result.get("listOfList").has(1));
        Assert.assertTrue(result.get("listOfList").get(1).has(0));
        Assert.assertTrue(result.get("listOfList").get(1).has(1));
        Assert.assertEquals("HELLO", result.get("listOfList").get(1).get(0).asText());
        Assert.assertEquals("WORLD", result.get("listOfList").get(1).get(1).asText());
    }

    /**
     * @since 16.01.18
     */
    @Test
    public void standardTest003() {
        final String fields = "attributes";
        final com.zero_x_baadf00d.partialize.Partialize partialize = new com.zero_x_baadf00d.partialize.Partialize();

        final JsonNode result = partialize.buildPartialObject(fields, BankAccountPojo.class, this.bankAccount);

        Assert.assertNotNull(result);
        Assert.assertTrue(result.has("attributes"));
        Assert.assertTrue(result.get("attributes").has("boolean"));
        Assert.assertTrue(result.get("attributes").has("null"));
        Assert.assertTrue(result.get("attributes").has("string"));
        Assert.assertTrue(result.get("attributes").has("double"));
        Assert.assertTrue(result.get("attributes").has("integer"));
    }

    /**
     * @since 16.01.18
     */
    @Test
    public void standardTest004() {
        final String fields = "attributes(boolean,integer),displayName";
        final com.zero_x_baadf00d.partialize.Partialize partialize = new com.zero_x_baadf00d.partialize.Partialize();

        final JsonNode result = partialize.buildPartialObject(fields, BankAccountPojo.class, this.bankAccount);

        Assert.assertNotNull(result);
        Assert.assertTrue(result.has("attributes"));
        Assert.assertTrue(result.has("displayName"));
        Assert.assertTrue(result.get("attributes").has("boolean"));
        Assert.assertTrue(result.get("attributes").has("integer"));
        Assert.assertFalse(result.get("attributes").has("null"));
        Assert.assertFalse(result.get("attributes").has("string"));
        Assert.assertFalse(result.get("attributes").has("double"));
    }

    /**
     * @since 16.02.21
     */
    @Test
    public void standardTest005() {
        final String fields = "isActive";
        final com.zero_x_baadf00d.partialize.Partialize partialize = new com.zero_x_baadf00d.partialize.Partialize();

        final JsonNode result = partialize.buildPartialObject(fields, BankAccountPojo.class, this.bankAccount);

        Assert.assertNotNull(result);
        Assert.assertFalse(result.has("isActive"));
    }

    /**
     * @since 16.09.27
     */
    @Test
    public void standardTest006() {
        final String fields = "attributes(boolean,*)";
        final com.zero_x_baadf00d.partialize.Partialize partialize = new com.zero_x_baadf00d.partialize.Partialize();

        final JsonNode result = partialize.buildPartialObject(fields, BankAccountPojo.class, this.bankAccount);

        Assert.assertNotNull(result);
        Assert.assertTrue(result.has("attributes"));
        Assert.assertTrue(result.get("attributes").has("boolean"));
        Assert.assertTrue(result.get("attributes").has("null"));
        Assert.assertTrue(result.get("attributes").has("string"));
        Assert.assertTrue(result.get("attributes").has("double"));
        Assert.assertTrue(result.get("attributes").has("integer"));
    }

    /**
     * BankAccountPojo.
     *
     * @author Thibault Meyer
     * @version 16.01.18
     * @since 16.01.18
     */
    @Partialize
    public static class BankAccountPojo {

        private String displayName;
        private Double amount;
        private List<List<String>> listOfList;
        private Map<String, Object> attributes;
        private BankAccountLevel level;
        private BankAccountNetwork network;

        private DateTime createDate;

        public List<List<String>> getListOfList() {
            return this.listOfList;
        }

        public void setListOfList(List<List<String>> listOfList) {
            this.listOfList = listOfList;
        }

        public Map<String, Object> getAttributes() {
            return this.attributes;
        }

        public void setAttributes(Map<String, Object> attributes) {
            this.attributes = attributes;
        }

        public String getDisplayName() {
            return this.displayName;
        }

        public void setDisplayName(String displayName) {
            this.displayName = displayName;
        }

        public Double getAmount() {
            return this.amount;
        }

        public void setAmount(Double amount) {
            this.amount = amount;
        }

        public DateTime getCreateDate() {
            return this.createDate;
        }

        public void setCreateDate(DateTime createDate) {
            this.createDate = createDate;
        }

        public boolean isActive() {
            return false;
        }

        public BankAccountLevel getLevel() {
            return level;
        }

        public void setLevel(final BankAccountLevel level) {
            this.level = level;
        }

        public BankAccountNetwork getNetwork() {
            return network;
        }

        public void setNetwork(final BankAccountNetwork network) {
            this.network = network;
        }
    }

    public enum BankAccountLevel {
        STANDARD
    }

    public enum BankAccountNetwork {
        NET_1("1");

        private final String networkId;

        BankAccountNetwork(final String networkId) {
            this.networkId = networkId;
        }

        @Override
        public String toString() {
            return this.networkId;
        }
    }
}
