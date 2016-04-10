package toolbox;

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
