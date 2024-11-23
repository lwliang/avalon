/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.core.enums;

import com.avalon.core.model.RecordColumn;
import com.avalon.core.util.ObjectUtils;

/**
 * 说明 字表操作
 */
public enum ServiceOperateEnum {

    none(0),
    insert(1),
    update(2),
    delete(4),
    del(3);

    private Integer code;


    ServiceOperateEnum(Integer code) {
        this.code = code;
    }

    public static ServiceOperateEnum getFromRecordColumn(RecordColumn column) {
        return getFromObj(column.getValue());
    }

    public static ServiceOperateEnum getFromObj(Object op) {
        if (ObjectUtils.isNull(op)) return none;
        if (op instanceof String) {
            return getFromString(op.toString());
        } else if (op instanceof ServiceOperateEnum) {
            return (ServiceOperateEnum) op;
        } else {
            return getFromInt(Integer.parseInt(op.toString()));
        }
    }

    public static ServiceOperateEnum getFromInt(Integer op) {
        return switch (op) {
            case 0 -> none;
            case 1 -> insert;
            case 2 -> update;
            default -> del;
        };
    }

    public static ServiceOperateEnum getFromString(String op) {
        return switch (op) {
            case "none" -> none;
            case "insert" -> insert;
            case "update" -> update;
            default -> del;
        };
    }
}
