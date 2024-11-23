/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.core.model;

import com.avalon.core.exception.AvalonException;
import com.avalon.core.field.Field;
import com.avalon.core.util.*;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@JsonSerialize(using = RecordRowSerializer.class)
@JsonDeserialize(using = RecordRowDeserializer.class)
public class RecordRow extends HashMap<String, RecordColumn> implements Serializable {

    private static final Logger log = LoggerFactory.getLogger(RecordRow.class);

    public RecordRow put(String fieldName, Object value) {
        if (FieldUtils.hasJoinSelect(fieldName)) {
            fieldName = FieldUtils.getJoinDisplayString(fieldName);
        }
        put(fieldName, new RecordColumn(value));
        return this;
    }

    public RecordRow put(String fieldName, FieldValue value) {
        if (FieldUtils.hasJoinSelect(fieldName)) {
            fieldName = FieldUtils.getJoinDisplayString(fieldName);
        }
        put(fieldName, new RecordColumn(value));
        return this;
    }

    public RecordRow put(Field field, Object value) {
        String fieldName = field.getName();
        if (FieldUtils.hasJoinSelect(fieldName)) {
            fieldName = FieldUtils.getJoinDisplayString(fieldName);
        }
        put(fieldName, new RecordColumn(value));
        return this;
    }

    public RecordRow put(Field field, FieldValue value) {
        String fieldName = field.getName();
        if (FieldUtils.hasJoinSelect(fieldName)) {
            fieldName = FieldUtils.getJoinDisplayString(fieldName);
        }
        put(fieldName, new RecordColumn(value));
        return this;
    }


    public RecordRow put(Field field, RecordColumn value) {
        String fieldName = field.getName();
        if (FieldUtils.hasJoinSelect(fieldName)) {
            fieldName = FieldUtils.getJoinDisplayString(fieldName);
        }
        put(fieldName, value);
        return this;
    }

    public RecordRow putNull(String fieldName) {
        if (FieldUtils.hasJoinSelect(fieldName)) {
            fieldName = FieldUtils.getJoinDisplayString(fieldName);
        }
        put(fieldName, new RecordColumn(null));
        return this;
    }

    public RecordRow putNull(Field field) {
        put(field, new RecordColumn(null));
        return this;
    }

    public Boolean containsKey(String fieldName) {
        if (FieldUtils.hasJoinSelect(fieldName)) {
            fieldName = FieldUtils.getJoinDisplayString(fieldName);
        }
        return super.containsKey(fieldName);
    }

    public Boolean containsKey(Field field) {
        return containsKey(field.getName());
    }

    public RecordRow() {
        super();
    }

    public static RecordRow build() {
        return new RecordRow();
    }

    public RecordRow(Map<String, Object> map) {
        map.forEach((key, item) -> {
            put(key, item);
        });
    }

    public Boolean isNull(String fieldName) {
        if (!containsKey(fieldName)) {
            return true;
        }
        return ObjectUtils.isNull(get(fieldName)) || get(fieldName).isEmpty();
    }

    public Boolean isNull(Field field) {
        if (!containsKey(field)) {
            return true;
        }

        return ObjectUtils.isNull(get(field)) || get(field).isEmpty();
    }

    public Boolean isNotNull(String fieldName) {
        return !isNull(fieldName);
    }

    public Boolean isNotNull(Field field) {
        return !isNull(field);
    }

    /**
     * 获取原始值
     *
     * @param fieldName
     * @return
     */
    public Object getRawValue(String fieldName) {
        if (!containsKey(fieldName)) {
            return null;
        }
        RecordColumn recordColumn = get(fieldName);
        if (ObjectUtils.isNull(recordColumn)) {
            return null;
        }
        return get(fieldName).getValue();
    }

    public Object getRawValue(Field field) {
        return getRawValue(field.getName());
    }


    /**
     * 转换为json字符串
     *
     * @return
     * @throws AvalonException
     */
    public String convert2Json() throws AvalonException {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new Jdk8Module());
            objectMapper.registerModule(new JavaTimeModule());
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

    /**
     * 将RecordRow转成Object
     *
     * @param clazz
     * @param <T>
     * @return
     * @throws AvalonException
     */
    public <T> T convert2Object(Class<T> clazz) throws AvalonException {
        return JacksonUtil.convert2Object(convert2Json(), clazz);
    }

    public Map<String, Object> convert2Map() {
        return JacksonUtil.convert2Object(convert2Json(), new TypeReference<Map>() {
        });
    }

    /**
     * 获取字段名的值
     *
     * @param fieldName
     * @return
     */
    public RecordColumn get(String fieldName) {
        if (StringUtils.isEmpty(fieldName)) return null;
        if (FieldUtils.hasJoinSelect(fieldName)) {
            fieldName = FieldUtils.getJoinDisplayString(fieldName);
        }
        return super.get(fieldName);
    }

    /**
     * 根据Field获取值
     *
     * @param field
     * @return
     */
    public RecordColumn get(Field field) {
        if (ObjectUtils.isNull(field)) return null;

        String name = field.getName();

        return get(name);
    }


    public Integer getInteger(Field field) {
        return get(field).getInteger();
    }

    public Integer getInteger(String fieldName) {
        return get(fieldName).getInteger();
    }

    public Record getRecord(Field field) {
        if (!containsKey(field)) return null;
        return get(field).getRecord();
    }

    public Record getRecord(String fieldName) {
        if (!containsKey(fieldName)) return null;
        return get(fieldName).getRecord();
    }

    public RecordRow getRecordRow(Field field) {
        return get(field).getRecordRow();
    }

    public RecordRow getRecordRow(String fieldName) {
        return get(fieldName).getRecordRow();
    }

    public String getString(Field field) {
        return get(field).getString();
    }

    public String getString(String fieldName) {
        return get(fieldName).getString();
    }

    public BigDecimal getBigDecimal(Field field) {
        return get(field).getBigDecimal();
    }

    public BigDecimal getBigDecimal(String fieldName) {
        return get(fieldName).getBigDecimal();
    }

    public Date getDate(Field field) {
        return get(field).getDate();
    }

    public Date getDate(String fieldName) {
        return get(fieldName).getDate();
    }

    public Float getFloat(Field field) {
        return get(field).getFloat();
    }

    public Float getFloat(String fieldName) {
        return get(fieldName).getFloat();
    }

    public Double getDouble(Field field) {
        return get(field).getDouble();
    }

    public Double getDouble(String fieldName) {
        return get(fieldName).getDouble();
    }

    public Long getLong(Field field) {
        return get(field).getLong();
    }

    public Long getLong(String fieldName) {
        return get(fieldName).getLong();
    }

    public Boolean getBoolean(Field field) {
        return get(field).getBoolean();
    }

    public Boolean getBoolean(String fieldName) {
        return get(fieldName).getBoolean();
    }

    public <T extends Enum<T>> T getValueOfEnum(Class<T> t, Field field) {
        return get(field).getValueOfEnum(t);
    }

    public <T extends Enum<T>> T getValueOfEnum(Class<T> t, String fieldName) {
        return get(fieldName).getValueOfEnum(t);
    }

    public FieldValue incrInteger(String fieldName, Integer incr) {
        return get(fieldName).incrInteger(incr);
    }

    public FieldValue incrInteger(Field field, Integer incr) {
        return get(field).incrInteger(incr);
    }

    public FieldValue incrString(String fieldName, String incr) {
        return get(fieldName).incrString(incr);
    }

    public FieldValue incrString(Field field, String incr) {
        return get(field).incrString(incr);
    }

    public FieldValue incrBigDecimal(String fieldName, BigDecimal incr) {
        return get(fieldName).incrBigDecimal(incr);
    }

    public FieldValue incrBigDecimal(Field field, BigDecimal incr) {
        return get(field).incrBigDecimal(incr);
    }

    public FieldValue incrDouble(String fieldName, Double incr) {
        return get(fieldName).incrDouble(incr);
    }

    public FieldValue incrDouble(Field field, Double incr) {
        return get(field).incrDouble(incr);
    }

    public FieldValue incrFloat(String fieldName, Float incr) {
        return get(fieldName).incrFloat(incr);
    }

    public FieldValue incrFloat(Field field, Float incr) {
        return get(field).incrFloat(incr);
    }

    public FieldValue incrLong(String fieldName, Long incr) {
        return get(fieldName).incrLong(incr);
    }

    public FieldValue incrLong(Field field, Long incr) {
        return get(field).incrLong(incr);
    }

    public FieldValue decrInteger(String fieldName, Integer decr) {
        return get(fieldName).decrInteger(decr);
    }

    public FieldValue decrInteger(Field field, Integer decr) {
        return get(field).decrInteger(decr);
    }

    public FieldValue decrBigDecimal(Field field, BigDecimal decr) {
        return get(field).decrBigDecimal(decr);
    }

    public FieldValue decrBigDecimal(String fieldName, BigDecimal decr) {
        return get(fieldName).decrBigDecimal(decr);
    }

    public FieldValue decrDouble(String fieldName, Double decr) {
        return get(fieldName).decrDouble(decr);
    }

    public FieldValue decrDouble(Field field, Double decr) {
        return get(field).decrDouble(decr);
    }

    public FieldValue decrFloat(Field field, Float decr) {
        return get(field).decrFloat(decr);
    }

    public FieldValue decrFloat(String fieldName, Float decr) {
        return get(fieldName).decrFloat(decr);
    }

    public FieldValue decrLong(String fieldName, Long decr) {
        return get(fieldName).decrLong(decr);
    }

    public FieldValue decrLong(Field field, Long decr) {
        return get(field).decrLong(decr);
    }

    public FieldValue multiplyInteger(String fieldName, Integer multi) {
        return get(fieldName).multiplyInteger(multi);
    }

    public FieldValue multiplyInteger(Field field, Integer multi) {
        return get(field).multiplyInteger(multi);
    }

    public FieldValue multiplyBigDecimal(String fieldName, BigDecimal multi) {
        return get(fieldName).multiplyBigDecimal(multi);
    }

    public FieldValue multiplyBigDecimal(Field field, BigDecimal multi) {
        return get(field).multiplyBigDecimal(multi);
    }

    public FieldValue multiplyDouble(String fieldName, Double multi) {
        return get(fieldName).multiplyDouble(multi);
    }

    public FieldValue multiplyDouble(Field field, Double multi) {
        return get(field).multiplyDouble(multi);
    }

    public FieldValue multiplyFloat(String fieldName, Float multi) {
        return get(fieldName).multiplyFloat(multi);
    }

    public FieldValue multiplyFloat(Field field, Float multi) {
        return get(field).multiplyFloat(multi);
    }

    public FieldValue multiplyLong(String fieldName, Long multi) {
        return get(fieldName).multiplyLong(multi);
    }

    public FieldValue multiplyLong(Field field, Long multi) {
        return get(field).multiplyLong(multi);
    }

    public FieldValue divideInteger(String fieldName, Integer divide) {
        return get(fieldName).divideInteger(divide);
    }

    public FieldValue divideInteger(Field field, Integer divide) {
        return get(field).divideInteger(divide);
    }

    public FieldValue divideBigDecimal(String fieldName, BigDecimal divide) {
        return get(fieldName).divideBigDecimal(divide);
    }

    public FieldValue divideBigDecimal(Field field, BigDecimal divide) {
        return get(field).divideBigDecimal(divide);
    }

    public FieldValue divideDouble(String fieldName, Double divide) {
        return get(fieldName).divideDouble(divide);
    }

    public FieldValue divideDouble(Field field, Double divide) {
        return get(field).divideDouble(divide);
    }

    public FieldValue divideFloat(String fieldName, Float divide) {
        return get(fieldName).divideFloat(divide);
    }

    public FieldValue divideFloat(Field field, Float divide) {
        return get(field).divideFloat(divide);
    }

    public FieldValue divideLong(String fieldName, Long divide) {
        return get(fieldName).divideLong(divide);
    }

    public FieldValue divideLong(Field field, Long divide) {
        return get(field).divideLong(divide);
    }

    public Boolean isRecordRow(String fieldName) {
        if (ObjectUtils.isNull(fieldName)) {
            return false;
        }

        if (isNull(fieldName)) {
            return false;
        }

        Object rawValue = getRawValue(fieldName);

        return rawValue instanceof RecordRow;
    }

}
