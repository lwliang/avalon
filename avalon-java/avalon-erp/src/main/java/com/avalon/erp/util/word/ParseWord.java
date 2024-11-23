/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.erp.util.word;

import com.avalon.core.field.Field;
import com.avalon.core.field.HtmlField;
import com.avalon.core.field.IFieldFormat;
import com.avalon.core.model.RecordColumn;
import com.avalon.core.model.RecordRow;
import com.avalon.core.service.IAvalonService;
import com.avalon.core.util.ObjectUtils;
import lombok.Data;

@Data
public class ParseWord {
    private String word;//原数据
    private int begin;//开始索引
    private int end;//结束索引
    private String field;//字段名称
    private IFieldFormat fieldFormat;//字段格式化

    /**
     * 渲染数据 当前字段的值
     *
     * @param row
     * @param service
     * @return
     */
    public RecordColumn getValue(RecordRow row, IAvalonService service) {
        if (ObjectUtils.isNull(service)) {
            return row.get(getField());
        }

        if (ObjectUtils.isNotNull(fieldFormat)) {
            return new RecordColumn(fieldFormat.getSqlValue(row.getRawValue(field)));
        }
        if (service.containField(field)) {
            Field field1 = service.getField(field);
            if (field1 instanceof IFieldFormat) {
                return new RecordColumn(field1.getSqlValue(row.getRawValue(field)));
            } else if (field1 instanceof HtmlField) {
                return new RecordColumn(((HtmlField) field1).toText(row.getRawValue(field).toString()));
            }
        }
        return row.get(getField());
    }
}
