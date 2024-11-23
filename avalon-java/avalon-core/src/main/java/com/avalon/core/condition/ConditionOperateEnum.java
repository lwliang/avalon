/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.core.condition;

import com.avalon.core.exception.AvalonException;
import com.avalon.core.model.SelectionHashMap;
import com.avalon.core.util.EnumUtils;

public enum ConditionOperateEnum {

    NotLike("(%s NOT like CONCAT('%%',%s,'%%'))", "notLike"),
    Equal("( %s = %s)", "="),
    NotEqual("(%s != %s)", "!="),
    Greater("(%s > %s)", ">"),
    Less("(%s < %s)", "<"),
    Like("(%s like CONCAT('%%',%s,'%%'))", "like"),
    GreaterEqual("(%s >= %s)", ">="),
    LessEqual("(%s <= %s)", "<="),
    Between("(%s BETWEEN %s AND %s)", "between"),
    And("(%s AND %s)", "&"),
    In("(%s IN (%s))", "in"),
    NotIn("(%s NOT IN (%s))", "notIn"),
    Or("( %s OR %s)", "|"),
    Span("((%s %s %s) %s %s)", "span");

    private final String value;

    private final String name;

    ConditionOperateEnum(String value, String name) {
        this.value = value;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public ConditionOperateEnum fromString(String condition) {
        return valueOf(condition);
    }

    public static SelectionHashMap getSelectionMap() throws AvalonException {
        return EnumUtils.getSelectionMapFromEnum(ConditionOperateEnum.class.getName());
    }
}
