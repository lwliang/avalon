/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.core.model;

import com.avalon.core.field.Field;
import com.avalon.core.field.RelationField;
import com.avalon.core.field.StringField;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class FieldSerializer extends JsonSerializer<Field> {
    @Override
    public void serialize(Field field, JsonGenerator jsonGenerator,
                          SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();

        jsonGenerator.writeObjectField("name", field.getName());
        jsonGenerator.writeObjectField("label", field.getLabel());
        jsonGenerator.writeObjectField("type", field.getClass().getSimpleName());
        jsonGenerator.writeObjectField("isRequired", field.isRequired());
        if (field instanceof StringField) {
            jsonGenerator.writeObjectField("length", ((StringField) field).getMaxLength());
        }
        if (field instanceof RelationField) {
            jsonGenerator.writeObjectField("relativeServiceName", ((RelationField) field).getRelativeServiceName());
        }

        jsonGenerator.writeEndObject();
    }
}
