/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.core.model;

import com.avalon.core.exception.AvalonException;
import com.avalon.core.util.JacksonUtil;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@JsonSerialize(using = RecordSerializer.class)
@JsonDeserialize(using = RecordDeserializer.class)
public class Record extends ArrayList<RecordRow> implements Serializable {
    private static final Logger log = LoggerFactory.getLogger(Record.class);

    public void putAll(String fieldName, Object value) {

        this.forEach((row) -> {
            row.put(fieldName, value);
        });
    }

    /**
     * 创建record
     *
     * @return record
     */
    public static Record build() {
        return new Record();
    }

    /**
     * 合并record
     *
     * @param record
     */
    public void join(Record record) {
        this.addAll(record);
    }


    public String convert2Json() throws AvalonException {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            JsonGenerator jsonGenerator = JacksonUtil.createJsonGenerator(bos);
            ObjectWriter objectWriter = objectMapper.writer();

            objectWriter.writeValue(jsonGenerator, this);
            jsonGenerator.flush();
            return bos.toString(StandardCharsets.UTF_8);

        } catch (Exception e) {
            throw new AvalonException(e.getMessage());
        }
    }

    public RecordRow getRecordRow(String field, Object value) {
        return this.stream().filter((recordRow -> recordRow.getRawValue(field).equals(value))).findFirst().orElse(null);
    }

    public List<Object> getValues(String field) {
        List<Object> values = new ArrayList<>();
        this.forEach((recordRow -> {
            Object rawValue = recordRow.getRawValue(field);
            if (!values.contains(rawValue)) {
                values.add(rawValue);
            }
        }));
        return values;
    }
}
