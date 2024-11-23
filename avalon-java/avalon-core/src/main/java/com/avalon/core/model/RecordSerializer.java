/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.core.model;

import com.avalon.core.util.ObjectUtils;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;
import java.util.Map;

@JsonComponent
public class RecordSerializer extends JsonSerializer<Record> {
    /**
     * 序列化操作,继承JsonSerializer，重写Serialize函数
     */
    @Override
    public void serialize(Record value, JsonGenerator jsonGenerator,
                          SerializerProvider serializerProvider) throws IOException {

        jsonGenerator.writeStartArray();
        for (RecordRow row : value) {
            jsonGenerator.writeStartObject();
            for (Map.Entry<String, RecordColumn> column : row.entrySet()) {
                if(ObjectUtils.isNull(column.getValue())){
                    jsonGenerator.writeObjectField(column.getKey(), null);
                    continue;
                }
                jsonGenerator.writeObjectField(column.getKey(), column.getValue().getClientValue());
            }
            jsonGenerator.writeEndObject();
        }
        jsonGenerator.writeEndArray();
    }
}
