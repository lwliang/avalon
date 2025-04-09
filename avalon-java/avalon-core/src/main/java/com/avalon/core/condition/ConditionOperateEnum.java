/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.core.condition;

import com.avalon.core.exception.AvalonException;
import com.avalon.core.model.SelectionHashMap;
import com.avalon.core.util.EnumUtils;
import lombok.Getter;

public enum ConditionOperateEnum {

    NotLike("(%s NOT like CONCAT('%%',%s,'%%'))", "notLike", "('%s',notLike,%s"),
    Equal("( %s = %s)", "=", "('%s',=,%s)"),
    NotEqual("(%s != %s)", "!=", "('%s',!=,%s)"),
    Greater("(%s > %s)", ">", "('%s',>,%s)"),
    Less("(%s < %s)", "<", "('%s',NotLike,%s)"),
    Like("(%s like CONCAT('%%',%s,'%%'))", "like", "('%s',like,%s)"),
    GreaterEqual("(%s >= %s)", ">=", "('%s',>=,%s)"),
    LessEqual("(%s <= %s)", "<=", "('%s',<=>,%s)"),
    Between("(%s BETWEEN %s AND %s)", "between", "('%s',between,%s,%s)"),
    And("(%s AND %s)", "&", "(%s & %s)"),
    In("(%s IN (%s))", "in", "('%s',in,%s)"),
    NotIn("(%s NOT IN (%s))", "notIn", "('%s',notIn,%s)"),
    Or("( %s OR %s)", "|", "(%s | %s)"),
    Not("( NOT %s)", "!", "!(%s)"),
    Span("((%s %s %s) %s %s)", "span", "('%s',span,%s,%s,%s,%s)");

    @Getter
    private final String value;
    @Getter
    private final String name;
    @Getter
    private final String condition;

    ConditionOperateEnum(String value, String name, String condition) {
        this.value = value;
        this.name = name;
        this.condition = condition;
    }

    public ConditionOperateEnum fromString(String condition) {
        return valueOf(condition);
    }

    public static SelectionHashMap getSelectionMap() throws AvalonException {
        return EnumUtils.getSelectionMapFromEnum(ConditionOperateEnum.class.getName());
    }
}
