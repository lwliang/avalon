/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.core.condition;

import com.avalon.core.exception.AvalonException;
import com.avalon.core.model.FieldValueStatement;
import com.avalon.core.model.JoinRelationMap;
import com.avalon.core.select.builder.SelectBuilder;
import com.avalon.core.service.AbstractService;

public interface ICondition {
    String getSql(AbstractService service, FieldValueStatement valueStatement);

    Object parse(AbstractService service) throws AvalonException;

    Boolean hasJoinSelect();

    String getSql(SelectBuilder builder);

    /**
     * 获得逆波兰字符串
     *
     * @return 逆波兰字符串
     */
    String getReversePolishNotation();

    /**
     * 解析逆波兰表达式
     *
     * @return condition
     * @Param conStr 表达式
     */
    Condition parseReversePolishNotation(String conStr);
}
