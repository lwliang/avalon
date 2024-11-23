/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.core.util;

import com.avalon.core.field.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FieldUtils {
    public static Field[] getAllFields(final Class<?> cls) {
        final List<Field> allFieldsList = getAllFieldsList(cls);
        return allFieldsList.toArray(new Field[allFieldsList.size()]);
    }

    /**
     * 获取所有声明的字段，含所有父类
     *
     * @param cls
     * @return
     */
    public static List<Field> getAllFieldsList(final Class<?> cls) {
        final List<Field> allFields = new ArrayList<Field>();
        Class<?> currentClass = cls;
        while (currentClass != null) {
            final Field[] declaredFields = currentClass.getDeclaredFields();
            for (final Field field : declaredFields) {
                allFields.add(field);
            }
            currentClass = currentClass.getSuperclass();
        }
        return allFields;
    }

    /**
     * 获取到某个父类的所有字段 含父类
     *
     * @param cls
     * @param superCls
     * @return
     */
    public static List<Field> getAllFieldsList(final Class<?> cls, final Class<?> superCls) {
        final List<Field> allFields = new ArrayList<Field>();
        Class<?> currentClass = cls;
        while (currentClass != superCls.getSuperclass()) {
            final Field[] declaredFields = currentClass.getDeclaredFields();
            Collections.addAll(allFields, declaredFields);
            currentClass = currentClass.getSuperclass();
        }
        return allFields;
    }

    /**
     * 获取到某个父类的所有字段 含父类的所有fieldSuperCls的子类字段
     *
     * @param cls
     * @param superCls
     * @param fieldSuperCls
     * @return
     */
    public static List<Field> getAllFieldsList(final Class<?> cls,
                                               final Class<?> superCls,
                                               final Class<?> fieldSuperCls) {
        final List<Field> allFields = new ArrayList<Field>();
        Class<?> currentClass = cls;
        while (currentClass != superCls.getSuperclass()) {
            final Field[] declaredFields = currentClass.getDeclaredFields();
            for (Field field : declaredFields) {
                if (com.avalon.core.field.Field.class.isAssignableFrom(field.getType())) {
                    Collections.addAll(allFields, field);
                }
            }
            currentClass = currentClass.getSuperclass();
        }
        return allFields;
    }

    /**
     * 返回管理字段的分割符号
     *
     * @return
     */
    public static String getJoinDivision() {
        return ".";
    }

    /**
     * 字段是否需要及联查询
     *
     * @param field
     * @return
     */
    public static Boolean hasJoinSelect(String field) {
        if (StringUtils.isEmpty(field)) return false;

        return field.contains(".");
    }

    /**
     * 将a.b.c 转换为aBC
     *
     * @param field
     * @return
     */
    public static String getJoinDisplayString(String field) {
        if (hasJoinSelect(field)) {
            return Fields.camelName(Fields.dot2UnderscoreName(field));
        }
        return field;
    }

    /**
     * 带有多级的字符串转列表
     *
     * @param field
     * @return
     */
    public static List<String> getJoinStringList(String field) {
        if (StringUtils.isEmpty(field)) return new ArrayList<>();
        return List.of(field.split("\\."));
    }

    /**
     * 获取第一级表字段
     *
     * @param field
     * @return
     */
    public static String getJoinFirstTableString(String field) {
        int i = field.indexOf(".");
        if (i < 0) return field;
        return field.substring(0, i);
    }

    /**
     * 获取第一级之后的所有字段
     *
     * @param field
     * @return
     */
    public static String getJoinFirstFieldString(String field) {
        int i = field.indexOf(".");

        return field.substring(i + 1);
    }

    /**
     * 获取所有表名
     *
     * @param field
     * @return
     */
    public static String getJoinTableString(String field) {
        int i = field.lastIndexOf(".");
        if (i < 0) return "";
        return field.substring(0, i);
    }

    /**
     * 获取字段名
     *
     * @param field
     * @return
     */
    public static String getJoinFieldString(String field) {
        int i = field.lastIndexOf(".");
        return field.substring(i + 1);
    }

    /**
     * 将列表转为多级字符串字段
     *
     * @param fields
     * @return
     */
    public static String getJoinString(List<String> fields) {
        return StringUtils.joinField(fields);
    }

    /**
     * 将列表转为多级字符串字段
     *
     * @param fields
     * @return
     */
    public static String getJoinString(List<String> fields, Integer size) {
        return StringUtils.joinField(fields.subList(0, size + 1));
    }

    /**
     * 将列表转为多级字符串字段
     *
     * @param fields
     * @return
     */
    public static String getJoinString(String... fields) {
        return StringUtils.joinField(List.of(fields));
    }

    public static List<String> getFieldList(String fields) {
        String[] split = fields.split(",");
        for (int i = 0; i < split.length; i++) {
            split[i] = StringUtils.trim(split[i].trim());
        }
        return List.of(split);
    }

    public static String[] getFieldArray(String fields) {
        return getFieldList(fields).toArray(new String[0]);
    }

    /**
     * 获取最深的字段 用于获取个数
     *
     * @param fields
     * @return
     */
    public static String getJoinMaxCountString(String fields) {
        List<String> fieldList = getFieldList(fields);

        Integer max = 0;
        String maxField = fieldList.get(0);
        for (String field : fieldList) {
            Integer count = getJoinCount(field);

            if (max < count) {
                maxField = field;
                max = count;
            }
        }

        return maxField;
    }

    public static Integer getJoinCount(String field) {
        return StringUtils.getCount(field, "\\.");
    }

    /**
     * 获取逗号
     *
     * @param fields
     * @return
     */
    public static String getCommaString(List<String> fields) {
        return StringUtils.join(fields);
    }

    public static String underscoreName(String propertyName) {
        return Fields.underscoreName(propertyName);
    }

    public static String camelName(String fieldName) {
        return Fields.camelName(fieldName);
    }


    public static boolean isNumber(com.avalon.core.field.Field field) {
        return field instanceof IntegerField
                || field instanceof FloatField
                || field instanceof DoubleField
                || field instanceof BigDecimalField
                || field instanceof BigIntegerField;
    }

    public static boolean isString(com.avalon.core.field.Field field) {
        return field instanceof StringField
                || field instanceof SelectionField
                || field instanceof ImageField;
    }

    /**
     * 是否是关联字段
     *
     * @param field
     * @return
     */
    public static boolean isRelation(com.avalon.core.field.Field field) {
        return field instanceof RelationField;
    }

    /**
     * 是否是 关联表
     *
     * @param field
     * @return
     */
    public static boolean isVirtualRelation(com.avalon.core.field.Field field) {
        return field instanceof One2oneField
                || field instanceof One2manyField
                || field instanceof Many2manyField;
    }
}
