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
package converters;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.zero_x_baadf00d.partialize.converter.Converter;
import org.joda.time.DateTime;

/**
 * JodaDateTimeConverter.
 *
 * @author Thibault Meyer
 * @version 16.03.22
 * @since 16.01.18
 */
public class JodaDateTimeConverter implements Converter<DateTime> {

    @Override
    public void convert(String fieldName, DateTime data, ObjectNode node) {
        node.put(fieldName, data.toString("yyyy-MM-dd'T'HH:mm:ss"));
    }

    @Override
    public void convert(String fieldName, DateTime data, ArrayNode node) {
        node.add(data.toString("yyyy-MM-dd'T'HH:mm:ss"));
    }

    @Override
    public Class<DateTime> getManagedObjectClass() {
        return DateTime.class;
    }
}
