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
import java.util.Map;

@JsonComponent
public class RecordDeserializer extends JsonDeserializer<Record> {
    @Override
    public Record deserialize(JsonParser p, DeserializationContext ctxt) throws IOException,
            JacksonException {
        ObjectMapper mapper = (ObjectMapper) p.getCodec();
        JsonNode node = mapper.readTree(p);

        Iterator<JsonNode> elements = node.elements();
        Record record = Record.build();
        while (elements.hasNext()) {
            JsonNode next = elements.next();
            RecordRow recordRow = mapper.convertValue(next, RecordRow.class);
            record.add(recordRow);
        }

        return record;
    }
}
