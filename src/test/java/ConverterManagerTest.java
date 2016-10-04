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

import com.zero_x_baadf00d.partialize.PartializeConverterManager;
import com.zero_x_baadf00d.partialize.converter.Converter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import converters.BigDecimalConverter;
import converters.JodaDateTimeConverter;

import java.util.ArrayList;
import java.util.List;

/**
 * ConverterManagerTest.
 *
 * @author Thibault Meyer
 * @version 16.10.04
 * @since 16.10.04
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ConverterManagerTest {

    /**
     * @since 16.10.04
     */
    @Before
    public void reset() {
        PartializeConverterManager.getInstance().removeAllConverters();
    }

    /**
     * @since 16.10.04
     */
    @Test
    public void converterManager001() {
        final Converter converter = new JodaDateTimeConverter();
        PartializeConverterManager.getInstance().registerConverter(converter);
        Assert.assertEquals(
            1,
            PartializeConverterManager.getInstance().count()
        );
        Assert.assertEquals(
            converter,
            PartializeConverterManager.getInstance().getConverter(converter.getManagedObjectClass())
        );
    }

    /**
     * @since 16.10.04
     */
    @Test
    public void converterManager002() {
        final Converter converter_1 = new JodaDateTimeConverter();
        final Converter converter_2 = new BigDecimalConverter();
        PartializeConverterManager.getInstance().registerConverters(converter_1, converter_2);
        Assert.assertEquals(
            2,
            PartializeConverterManager.getInstance().count()
        );
        Assert.assertEquals(
            converter_1,
            PartializeConverterManager.getInstance().getConverter(converter_1.getManagedObjectClass())
        );
        Assert.assertEquals(
            converter_2,
            PartializeConverterManager.getInstance().getConverter(converter_2.getManagedObjectClass())
        );
    }

    /**
     * @since 16.10.04
     */
    @Test
    public void converterManager003() {
        final List<Converter> converters = new ArrayList<Converter>() {{
            add(new JodaDateTimeConverter());
            add(new BigDecimalConverter());
        }};
        PartializeConverterManager.getInstance().registerConverters(converters);
        Assert.assertEquals(
            2,
            PartializeConverterManager.getInstance().count()
        );
        Assert.assertEquals(
            converters.get(0),
            PartializeConverterManager.getInstance().getConverter(converters.get(0).getManagedObjectClass())
        );
        Assert.assertEquals(
            converters.get(1),
            PartializeConverterManager.getInstance().getConverter(converters.get(1).getManagedObjectClass())
        );
    }

    /**
     * @since 16.10.04
     */
    @Test
    public void converterManager004() {
        Assert.assertEquals(0, PartializeConverterManager.getInstance().count());
    }
}
