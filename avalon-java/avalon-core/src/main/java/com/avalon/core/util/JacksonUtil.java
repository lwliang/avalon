/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.core.util;

import com.avalon.core.exception.AvalonException;
import com.avalon.core.exception.JsonDecodeException;
import com.avalon.core.model.RecordRow;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.util.Map;

/**
 * jackson-core：核心包
 * jackson-annotations：注解包
 * jackson-databind：数据绑定包
 */
public class JacksonUtil {
    private static final JsonFactory jsonFactory = new JsonFactory();
    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
    }

    public static JsonGenerator createJsonGenerator(ByteArrayOutputStream bos) throws AvalonException {
        try {
            return jsonFactory.createGenerator(new PrintWriter(bos));
        } catch (Exception e) {
            throw new AvalonException(e.getMessage());
        }
    }

    public static RecordRow convert2Map(String json) throws AvalonException {
        try {
            return objectMapper.readValue(json, RecordRow.class);
        } catch (Exception e) {
            throw new JsonDecodeException(e.getMessage());
        }
    }

    public static RecordRow convert2Map(Object obj) throws AvalonException {
        return convert2Map(object2String(obj));
    }

    /**
     * 将json字符串转Object
     *
     * @param json
     * @param clazz
     * @param <T>
     * @return
     * @throws AvalonException
     */
    public static <T> T convert2Object(String json, Class<T> clazz) throws AvalonException {
        try {
            return objectMapper.readValue(json, clazz);
        } catch (Exception e) {
            throw new JsonDecodeException(e.getMessage());
        }
    }

    public static <T> T convert2Object(String json, TypeReference<T> typeReference) throws AvalonException {
        try {
            return objectMapper.readValue(json, typeReference);
        } catch (Exception e) {
            throw new JsonDecodeException(e.getMessage());
        }
    }

    public static <T> T convert2Object(JsonNode json, TypeReference<T> typeReference) throws AvalonException {
        try {
            return objectMapper.convertValue(json, typeReference);
        } catch (Exception e) {
            throw new JsonDecodeException(e.getMessage());
        }
    }

    /**
     * 将对象转成字符串
     *
     * @param obj
     * @return
     */
    public static String object2String(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new AvalonException(e.getMessage());
        }
    }

    /**
     * 将Map转成指定的Bean
     *
     * @param map
     * @param clazz
     * @return
     */
    public static Object map2Bean(Map map, Class<?> clazz) {
        try {
            return objectMapper.convertValue(map, clazz);
        } catch (Exception e) {
            throw new AvalonException(e.getMessage());
        }
    }

    /**
     * 将Bean转成Map
     *
     * @param obj
     * @return
     */
    public static Map bean2Map(Object obj) {
        try {
            return objectMapper.convertValue(obj, Map.class);
        } catch (Exception e) {
            throw new AvalonException(e.getMessage());
        }
    }

    public static <T> T convert2Object(JsonNode obj, Class<T> clazz) throws AvalonException {
        ObjectMapper mapper = objectMapper;
        try {
            return mapper.treeToValue(obj, clazz);
        } catch (JsonProcessingException e) {
            throw new AvalonException(e.getMessage(), e);
        }
    }
}
