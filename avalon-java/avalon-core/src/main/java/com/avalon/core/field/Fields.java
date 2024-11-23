/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.core.field;

import com.avalon.core.context.Context;
import com.avalon.core.face.field.BooleanFieldDefaultValue;
import com.avalon.core.face.field.DateTimeFieldDefaultValue;
import com.avalon.core.face.field.IFieldDefaultValue;
import com.avalon.core.face.field.IntegerFieldDefaultValue;
import com.avalon.core.model.SelectionHashMap;
import com.avalon.core.util.EnumUtils;

public class Fields {

    /**
     * 将驼峰式命名的字符串转换为下划线大写方式。如果转换前的驼峰式命名的字符串为空，则返回空字符串。
     * 例如：HelloWorld->hello_world
     *
     * @param label 转换前的驼峰式命名的字符串
     * @return 转换后下划线大写方式命名的字符串
     */
    public static String underscoreName(String label) {
        StringBuilder result = new StringBuilder();
        if (label != null && label.length() > 0) {
            // 将第一个字符处理成大写
            result.append(label.substring(0, 1).toLowerCase());
            // 循环处理其余字符
            for (int i = 1; i < label.length(); i++) {
                String s = label.substring(i, i + 1);
                // 在大写字母前添加下划线
                if (Character.isLetter(s.charAt(0)) && s.equals(s.toUpperCase())) {
                    result.append("_");

                }
                // 其他字符直接转成大写
                result.append(s.toLowerCase());

            }
        }
        return result.toString();
    }

    public static String dot2UnderscoreName(String label) {
        return label.replace(".", "_");
    }

    /**
     * 将下划线大写方式命名的字符串转换为驼峰式。如果转换前的下划线大写方式命名的字符串为空，则返回空字符串。
     * 例如：hello_world->helloWorld
     *
     * @param label 转换前的下划线大写方式命名的字符串
     * @return 转换后的驼峰式命名的字符串
     */
    public static String camelName(String label) {
        StringBuilder result = new StringBuilder();
        // 快速检查
        if (label == null || label.isEmpty()) {
            // 没必要转换
            return "";
        } else if (!label.contains("_")) {
            // 不含下划线，仅将首字母小写
            return label.substring(0, 1).toLowerCase() + label.substring(1);
        }
        // 用下划线将原始字符串分割
        String camels[] = label.split("_");
        for (String camel : camels) {
            // 跳过原始字符串中开头、结尾的下换线或双重下划线
            if (camel.isEmpty()) {
                continue;
            }
            // 处理真正的驼峰片段
            if (result.length() == 0) {
                // 第一个驼峰片段，全部字母都小写
                result.append(camel);
            } else {
                // 其他的驼峰片段，首字母大写 ，其余字母不变
                result.append(camel.substring(0, 1).toUpperCase());
                result.append(camel.substring(1));
            }
        }
        return result.toString();
    }


    /**
     * 创建密文字段
     *
     * @param label
     * @return
     */
    public static Field createPasswordField(String label) {
        PasswordField.Builder builder = new PasswordField.Builder();
        builder.setLabel(label);

        return builder.build();
    }

    /**
     * 选择字段类型
     *
     * @return 字段选择值
     */
    public static FieldSelectionField createFieldSelectionField() {
        return new FieldSelectionField("类型选择");
    }

    public static BigIntegerField createBigIntegerField(String label) {
        return new BigIntegerField(label);
    }

    public static BigIntegerField createBigIntegerField(Boolean isPrimaryKey, String label) {
        return new BigIntegerField(label, isPrimaryKey);
    }

    public static BigIntegerField createBigIntegerField(String label, Boolean isRequired) {
        return new BigIntegerField(label, isRequired, false);
    }

    public static BigIntegerField createBigIntegerField(String label,
                                                        Boolean isRequired,
                                                        Boolean isAutoIncrement,
                                                        Boolean isPrimaryKey) {
        return new BigIntegerField(label, isRequired, false, null, isPrimaryKey, isAutoIncrement);
    }

    public static IntegerField createInteger(String label) {
        return new IntegerField(label);
    }

    public static IntegerField createInteger(String label, Integer defaultValue) {
        return new IntegerField(label, defaultValue);
    }

    public static IntegerField createInteger(String label, Boolean isRequired) {
        return new IntegerField(label, isRequired);
    }

    public static IntegerField createInteger(Boolean allowNull, String label) {
        IntegerField integerField = new IntegerField(label);
        integerField.setAllowNull(allowNull);
        return integerField;
    }

    public static IntegerField createInteger(String label, Boolean isRequired, Integer min, Integer max) {
        return new IntegerField(label, isRequired, false, min, max);
    }

    public static IntegerField createInteger(String label, Integer min, Integer max) {
        return new IntegerField(label, false, false, min, max);
    }

    public static IntegerField createInteger(String label, Integer min, Integer max, Integer defaultValue) {
        IntegerField t = new IntegerField(label, false, false, min, max);
        t.setDefaultValue(new IntegerFieldDefaultValue(defaultValue));
        return t;
    }


    public static IntegerField createInteger(String label, Boolean isPrimaryKey, Boolean isAutoIncrement) {
        return new IntegerField(label, isPrimaryKey, isAutoIncrement);
    }

    public static StringField createString(String label) {
        return new StringField(label);
    }

    public static StringField createString(String label, Boolean isUnique) {
        return new StringField(label, isUnique);
    }

    public static StringField createString(String label, Integer length) {
        return new StringField(label, length);
    }

    public static StringField createString(String label, Integer length, Boolean isUnique) {
        return new StringField(label, isUnique, length);
    }

    public static StringField createString(String label, Boolean isRequired, Integer length) {
        return new StringField(label, isRequired, length);
    }

    public static StringField createString(Boolean allowNull, String label, Boolean isRequired, Integer length) {
        StringField stringField = new StringField(label, isRequired, length);
        stringField.setAllowNull(allowNull);
        return stringField;
    }

    public static StringField createString(String label, Boolean isRequired, Boolean isUnique, Integer length) {
        return new StringField(label, isRequired, isUnique, length);
    }

    public static StringField createString(String label, Integer length, String defaultValue) {
        return new StringField(label, length, defaultValue);
    }

    public static TextField createText(String label) {
        return new TextField(label);
    }

    public static TextField createText(String label, Boolean isRequired) {
        TextField textField = new TextField(label);
        textField.setIsRequired(isRequired);
        return textField;
    }

    public static HtmlField createHtml(String label) {
        return new HtmlField(label);
    }

    public static HtmlField createHtml(String label, Integer length) {
        return new HtmlField(label, length);
    }

    public static SelectionField createSelection(String label, Class<? extends Enum> enumClass) {
        return new SelectionField(label, enumClass);
    }

    public static SelectionField createSelection(String label, Class<? extends Enum> enumClass, Enum defaultValue) {
        return new SelectionField(label, enumClass, defaultValue);
    }

    public static SelectionField createSelection(String label,
                                                 SelectionHashMap selection) {
        return new SelectionField(label, selection);
    }

    public static SelectionField createSelection(String label, Boolean isRequired,
                                                 SelectionHashMap selection) {
        return new SelectionField(label, isRequired, selection);
    }

    public static SelectionField createSelection(String label,
                                                 Boolean isRequired,
                                                 SelectionHashMap selection,
                                                 IFieldDefaultValue defaultValue) {
        return new SelectionField(label, isRequired, selection, defaultValue);
    }

    public static SelectionField createSelection(String label,
                                                 SelectionHashMap selection,
                                                 IFieldDefaultValue defaultValue) {
        return new SelectionField(label, false, selection, defaultValue);
    }

    public static <T> SelectionField createSelection(String label,
                                                     SelectionHashMap selection,
                                                     T defaultValue) {
        return new SelectionField(label, false, selection, new IFieldDefaultValue() {
            @Override
            public Object getDefault(Context context) {
                return defaultValue;
            }

            @Override
            public String getDefaultString() {
                return "";
            }

            @Override
            public void setDefaultString(String scriptValue) {

            }

            @Override
            public Object getDefaultValue() {
                return null;
            }
        });
    }

    public static SelectionField createSelection(String label,
                                                 SelectionHashMap selection, Boolean isMulti) {
        return new SelectionField(label, selection, isMulti);
    }

    public static SelectionField createSelection(String label,
                                                 Class<? extends Enum> enumClass, Boolean isMulti) {
        return new SelectionField(label, EnumUtils.getSelectionMapFromEnum(enumClass), isMulti);
    }

    public static One2manyField createOne2many(String relativeServiceName,
                                               String relativeFieldName) {
        return new One2manyField(relativeServiceName, relativeFieldName);
    }

    public static One2manyField createOne2many(String label,
                                               String relativeServiceName,
                                               String relativeFieldName) {
        return new One2manyField(label, relativeServiceName, relativeFieldName);
    }

    public static One2oneField createOne2One(String relativeServiceName,
                                             String relativeFieldName) {
        return new One2oneField(relativeServiceName, relativeFieldName);
    }

    public static One2oneField createOne2One(String label, String relativeServiceName,
                                             String relativeFieldName) {
        return new One2oneField(label, relativeServiceName, relativeFieldName);
    }

    public static One2oneField createOne2One(String label,
                                             String relativeServiceName,
                                             String relativeFieldName,
                                             Boolean isRequired) {
        One2oneField one2oneField = new One2oneField(label, relativeServiceName, relativeFieldName);
        one2oneField.setIsRequired(isRequired);
        return one2oneField;
    }


    public static Many2oneField createMany2one(String label, String relativeServiceName) {
        return new Many2oneField(label, false, relativeServiceName);
    }

    public static Many2oneField createMany2one(String label, String relativeServiceName, Boolean isRequired) {
        return new Many2oneField(label, isRequired, relativeServiceName);
    }

    public static Many2oneField createMany2one(String label, String relativeServiceName, Integer defaultValue) {
        Many2oneField many2oneField = new Many2oneField(label, relativeServiceName);
        many2oneField.setDefaultValue(new IntegerFieldDefaultValue(defaultValue));
        return many2oneField;
    }

    public static Field createMany2many(String label,
                                        String relativeServiceName,
                                        String tableName,
                                        String masterFieldName,
                                        String relativeFieldName) {
        Many2manyField.Builder builder = Many2manyField.Builder.getInstance();

        builder.setLabel(label)
                .setRelativeKeyName(relativeFieldName)
                .setMasterKeyName(masterFieldName)
                .setTableName(tableName)
                .setRelativeServiceName(relativeServiceName);

        return builder.build();
    }

    public static Many2manyField createMany2many(String label, String relativeServiceName) {
        return new Many2manyField(label, relativeServiceName);
    }

    public static ImageField createImage(String label) {
        return new ImageField(label);
    }

    public static FloatField createFloat(String label) {
        return new FloatField(label);
    }

    public static FloatField createFloat(String label, Float mix, Float max) {
        return new FloatField(label, mix, max);
    }

    public static FloatField createFloat(String label, Boolean isRequired, Float mix, Float max) {
        return new FloatField(label, isRequired, false, mix, max);
    }

    public static DoubleField createDouble(String label) {
        return new DoubleField(label);
    }

    public static BigDecimalField createBigDecimal(String label) {
        return new BigDecimalField(label);
    }

    public static BigDecimalField createBigDecimal(String label, Boolean isRequired, Integer size, Integer scale) {
        return new BigDecimalField(label, isRequired, size, scale);
    }

    public static BigDecimalField createBigDecimal(String label, Integer size, Integer scale) {
        return new BigDecimalField(label, false, size, scale);
    }

    public static DateTimeField createDateTime(String label) {
        return new DateTimeField(label);
    }

    public static DateTimeField createDateTime(String label, Boolean isRequired) {
        return new DateTimeField(label, isRequired);
    }

    public static DateTimeField createDateTime(Boolean allowNull, String label) {
        DateTimeField temp = new DateTimeField(label);
        temp.setAllowNull(allowNull);
        return temp;
    }

    public static DateTimeField createDateTime(String label, Boolean isRequired, DateTimeFieldDefaultValue defaultValue) {
        return new DateTimeField(label, isRequired, defaultValue);
    }

    public static DateField createDate(String label) {
        return new DateField(label);
    }

    public static DateField createDate(String label, Boolean isRequired) {
        return new DateField(label, isRequired);
    }

    public static DateField createDate(String label, Boolean isRequired, String format) {
        return new DateField(label, isRequired, format);
    }

    public static DateField createDate(String label, Boolean isRequired, DateTimeFieldDefaultValue defaultValue) {
        DateField t = new DateField(label, isRequired);
        t.setDefaultValue(defaultValue);
        return t;
    }

    public static TimeField createTime(String label) {
        return new TimeField(label);
    }

    public static TimestampField createTimestamp(String label) {
        return new TimestampField(label);
    }

    public static BooleanField createBoolean(String label) {
        return new BooleanField(label);
    }

    public static BooleanField createBoolean(String label, Boolean isRequired) {
        return new BooleanField(label, isRequired, false, IFieldDefaultValue.getBooleanFieldDefaultValue());
    }

    public static BooleanField createBoolean(String label, Boolean isRequired, Boolean defaultValue) {
        return new BooleanField(label, isRequired, false, new BooleanFieldDefaultValue(defaultValue));
    }

}
