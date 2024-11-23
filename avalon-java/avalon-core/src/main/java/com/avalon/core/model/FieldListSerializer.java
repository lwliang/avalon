/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.core.model;

import com.avalon.core.field.Field;
import com.avalon.core.field.FieldList;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class FieldListSerializer extends JsonSerializer<FieldList> {
    @Override
    public void serialize(FieldList fields, JsonGenerator jsonGenerator,
                          SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartArray();
        FieldSerializer fieldSerializer = new FieldSerializer();
        for (Field field : fields) {
            jsonGenerator.writeStartObject();
            fieldSerializer.serialize(field, jsonGenerator, serializerProvider);
            jsonGenerator.writeEndObject();
        }
        jsonGenerator.writeEndArray();
    }
}
