/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.core.util;

import com.avalon.core.exception.AvalonException;
import com.avalon.core.field.*;
import com.avalon.core.model.FieldHashMap;
import com.avalon.core.model.FieldRecord;
import com.avalon.core.model.Record;
import com.avalon.core.model.RecordRow;
import com.avalon.core.service.AbstractService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class RecordRowUtils {
    public static RecordRow convert(Map<String, Object> value) {
        RecordRow recordRow = new RecordRow();

        value.forEach((key, item) -> {
            if (item instanceof List) {
                recordRow.put(key, RecordUtils.convert((ArrayList<Map<String, Object>>) item));
            } else if (item instanceof Map) {
                recordRow.put(key, RecordRowUtils.convert((Map<String, Object>) item));
            } else {
                recordRow.put(key, item);
            }

        });

        return recordRow;
    }

    /**
     * 根据字段进行格式化输入值
     *
     * @param service
     * @param value
     * @return
     * @throws AvalonException
     */
    public static RecordRow convert(AbstractService service, Map<String, Object> value)
            throws AvalonException {
//        RecordRow recordRow = new RecordRow();
//
//        for (Map.Entry<String, Object> keyValue : value.entrySet()) {
//            String key = keyValue.getKey();
//            Object item = keyValue.getValue();
//            Field field = service.getField(key);
//
//            if (item instanceof List) {
//
//                RelationField relationField = (RelationField) field;
//                if (ObjectUtils.isNull(relationField)) {
//                    continue;
//                }
////                AbstractService serviceBean = service.getServiceBean(relationField.getRelativeServiceName());
//                recordRow.put(key, RecordUtils.convert(relationField.getRelativeService(), (ArrayList<Map<String, Object>>) item));
//            } else if (item instanceof Map) {
//                RelationField relationField = (RelationField) field;
//                if (ObjectUtils.isNull(relationField)) {
//                    continue;
//                }
////                AbstractService serviceBean = service.getServiceBean(relationField.getRelativeServiceName());
//                recordRow.put(key, RecordRowUtils.convert(relationField.getRelativeService(), (Map<String, Object>) item));
//            } else {
//                if (ObjectUtils.isNotNull(field) &&
//                        field instanceof IFieldFormat) {//判断字段是否需要格式化
//                    Object parse = field.parse(item);
//                    recordRow.put(key, parse);
//                } else {
//                    recordRow.put(key, item);
//                }
//            }
//        }

//        return recordRow;
        RecordRow convert = convert(value);
        parsePush(service, convert);
        return convert;
    }

    /**
     * 格式化 字段 递归
     *
     * @param service
     * @param row
     */
    public static void parseRecursion(AbstractService service, RecordRow row) {

        FieldHashMap fieldFormatMap = service.getFieldFormatMap();

        fieldFormatMap.forEach((fieldName, field) -> {
            if (row.containsKey(fieldName)) {
                Object parse = field.parse(row.getRawValue(fieldName));
                row.put(fieldName, parse);
            }
        });

        FieldHashMap relationFieldMap = service.getRelationFieldMap();
        relationFieldMap.forEach((fieldName, field) -> {
            if (row.containsKey(fieldName)) {
                if (field instanceof One2oneField) {
                    RecordRow recordRow = row.getRecordRow(fieldName);
                    parseRecursion(((One2oneField) field).getRelativeService(), recordRow);
                } else if (field instanceof One2manyField) {
                    Record record = row.getRecord(fieldName);
                    parseRecursion(((One2manyField) field).getRelativeService(), record);
                }
            }
        });
    }

    public static void parseRecursion(AbstractService service, Record record) {

        FieldHashMap fieldFormatMap = service.getFieldFormatMap();
        FieldHashMap relationFieldMap = service.getRelationFieldMap();
        record.forEach(row -> {
            fieldFormatMap.forEach((fieldName, field) -> {
                if (row.containsKey(fieldName)) {
                    Object parse = field.parse(row.getRawValue(fieldName));
                    row.put(fieldName, parse);
                }
            });
            relationFieldMap.forEach((fieldName, field) -> {
                if (row.containsKey(fieldName)) {
                    if (field instanceof One2oneField) {
                        RecordRow recordRow = row.getRecordRow(fieldName);
                        parseRecursion(((One2oneField) field).getRelativeService(), recordRow);
                    } else if (field instanceof One2manyField) {
                        Record recordX = row.getRecord(fieldName);
                        parseRecursion(((One2manyField) field).getRelativeService(), recordX);
                    }
                }
            });
        });
    }

    /**
     * 格式化 采用堆栈的方式实现
     *
     * @param service
     * @param row
     */
    public static void parsePush(AbstractService service, RecordRow row) {

        FieldHashMap fieldFormatMap = service.getFieldFormatMap();

        fieldFormatMap.forEach((fieldName, field) -> {
            if (row.containsKey(fieldName)) {
                Object parse = field.parse(row.getRawValue(fieldName));
                row.put(fieldName, parse);
            }
        });

        FieldHashMap relationFieldMap = service.getRelationFieldMap();
        Queue<FieldRecord> push = new ConcurrentLinkedQueue<>();//判断是否还有未格式化的记录
        //先一次性处理前二级
        relationFieldMap.forEach((fieldName, field) -> {
            if (row.containsKey(fieldName)) {
                if (field instanceof One2oneField) {
                    AbstractService relativeService = ((One2oneField) field).getRelativeService();
                    FieldHashMap fieldFormatMap1 = relativeService.getFieldFormatMap();
                    RecordRow recordRow = row.getRecordRow(fieldName);
                    fieldFormatMap1.forEach((fieldName1, field1) -> {
                        if (recordRow.containsKey(fieldName1)) {
                            Object parse = field1.parse(recordRow.getRawValue(fieldName1));
                            recordRow.put(fieldName1, parse);
                        }
                    });
                    FieldHashMap relationFieldMap1 = relativeService.getRelationFieldMap();
                    relationFieldMap1.forEach((fieldName2, field2) -> {
                        if (recordRow.containsKey(fieldName2)) {
                            if (field2 instanceof One2manyField || field2 instanceof One2oneField) {
                                push.add(new FieldRecord(field2, recordRow.getRawValue(fieldName2)));
                            }
                        }
                    });
                } else if (field instanceof One2manyField) {
                    AbstractService relativeService = ((One2manyField) field).getRelativeService();
                    FieldHashMap fieldFormatMap2 = relativeService.getFieldFormatMap();
                    Record record = row.getRecord(fieldName);
                    FieldHashMap relationFieldMap1 = relativeService.getRelationFieldMap();

                    for (RecordRow recordRow : record) {
                        fieldFormatMap2.forEach((fieldName4, field4) -> {
                            if (recordRow.containsKey(fieldName4)) {
                                Object parse = field4.parse(recordRow.getRawValue(fieldName4));
                                recordRow.put(fieldName4, parse);
                            }
                        });

                        relationFieldMap1.forEach((fieldName2, field2) -> {
                            if (recordRow.containsKey(fieldName2)) {
                                if (field2 instanceof One2manyField || field2 instanceof One2oneField) {
                                    push.add(new FieldRecord(field2, recordRow.getRawValue(fieldName2)));
                                }
                            }
                        });
                    }
                }

            }
        });


        while (push.size() > 0) {
            FieldRecord poll = push.poll();

            Field field = poll.getField();
            if (field instanceof One2manyField) {

                AbstractService relativeService = ((One2manyField) field).getRelativeService();
                FieldHashMap fieldFormatMap2 = relativeService.getFieldFormatMap();
                Record record = (Record) poll.getValue();
                FieldHashMap relationFieldMap1 = relativeService.getRelationFieldMap();

                for (RecordRow recordRow : record) {
                    fieldFormatMap2.forEach((fieldName4, field4) -> {
                        if (recordRow.containsKey(fieldName4)) {
                            Object parse = field4.parse(recordRow.getRawValue(fieldName4));
                            recordRow.put(fieldName4, parse);
                        }
                    });

                    relationFieldMap1.forEach((fieldName2, field2) -> {
                        if (recordRow.containsKey(fieldName2)) {
                            if (field2 instanceof One2manyField || field2 instanceof One2oneField) {
                                push.add(new FieldRecord(field2, recordRow.getRawValue(fieldName2)));
                            }
                        }
                    });
                }
            }
            if (field instanceof One2oneField) {
                AbstractService relativeService = ((One2oneField) field).getRelativeService();
                FieldHashMap fieldFormatMap1 = relativeService.getFieldFormatMap();
                RecordRow recordRow = (RecordRow) poll.getValue();
                fieldFormatMap1.forEach((fieldName1, field1) -> {
                    if (recordRow.containsKey(fieldName1)) {
                        Object parse = field1.parse(recordRow.getRawValue(fieldName1));
                        recordRow.put(fieldName1, parse);
                    }
                });
                FieldHashMap relationFieldMap1 = relativeService.getRelationFieldMap();
                relationFieldMap1.forEach((fieldName2, field2) -> {
                    if (recordRow.containsKey(fieldName2)) {
                        if (field2 instanceof One2oneField || field2 instanceof One2manyField) {
                            push.add(new FieldRecord(field2, recordRow.getRawValue(fieldName2)));
                        }
                    }
                });
            }
        }
    }
}
