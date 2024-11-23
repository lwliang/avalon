/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.core.model;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@JsonComponent
public class RecordRowDeserializer extends JsonDeserializer<RecordRow> {
    @Override
    public RecordRow deserialize(JsonParser p, DeserializationContext ctxt) throws IOException,
            JacksonException {
        ObjectMapper mapper = (ObjectMapper) p.getCodec();
        JsonNode node = mapper.readTree(p);
        RecordRow row = RecordRow.build();
        Iterator<Map.Entry<String, JsonNode>> fields = node.fields();
        while (fields.hasNext()) {
            Map.Entry<String, JsonNode> field = fields.next();
            String key = field.getKey();
            JsonNode value = field.getValue();
            RecordColumn obj = null;
            if (value.isNull()) {
                obj = new RecordColumn(null);
            } else if (value.isBoolean()) {
                obj = new RecordColumn(value.asBoolean());
            } else if (value.isBigDecimal()) {
                obj = new RecordColumn(value.decimalValue());
            } else if (value.isFloat()) {
                obj = new RecordColumn(value.floatValue());
            } else if (value.isDouble()) {
                obj = new RecordColumn(value.doubleValue());
            } else if (value.isInt()) {
                obj = new RecordColumn(value.intValue());
            } else if (value.isLong()) {
                obj = new RecordColumn(value.longValue());
            } else if (value.isShort()) {
                obj = new RecordColumn(value.shortValue());
            } else if (value.isBigInteger()) {
                obj = new RecordColumn(value.bigIntegerValue());
            } else if (value.isTextual()) {
                obj = new RecordColumn(value.textValue());
            } else if (value.isArray()) {
                if(value.isEmpty()) {
                    obj = new RecordColumn(Record.build());
                } else {
                    if (value.get(0).isObject()) {
                        Record record = mapper.convertValue(value, Record.class);
                        obj = new RecordColumn(record);
                    } else {
                        List list = mapper.convertValue(value, List.class);
                        obj = new RecordColumn(list);
                    }
                }
            } else if(value.isObject()) {
                RecordRow recordRow = mapper.convertValue(value, RecordRow.class);
                obj = new RecordColumn(recordRow);
            }
            row.put(key, obj);
        }

        return row;
    }
}
