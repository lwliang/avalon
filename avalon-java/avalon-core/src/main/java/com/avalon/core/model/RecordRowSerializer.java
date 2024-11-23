/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.core.model;

import com.avalon.core.util.ObjectUtils;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;
import java.util.Map;

@JsonComponent
public class RecordRowSerializer extends JsonSerializer<RecordRow> {

    @Override
    public void serialize(RecordRow value, JsonGenerator jsonGenerator,
                          SerializerProvider serializers) throws IOException {
        jsonGenerator.writeStartObject();
        for (Map.Entry<String, RecordColumn> column : value.entrySet()) {
            if (ObjectUtils.isNull(column.getValue()) || column.getValue().isNull()) {
                jsonGenerator.writeObjectField(column.getKey(), null);
                continue;
            }
            if (column.getValue().getValue() instanceof RecordRow) {
                jsonGenerator.writeFieldName(column.getKey());
                RecordRow clientValue = (RecordRow) column.getValue().getClientValue();
                serialize(clientValue, jsonGenerator, serializers);
                continue;
            }
            jsonGenerator.writeObjectField(column.getKey(), column.getValue().getClientValue());
        }
        jsonGenerator.writeEndObject();
    }
}
