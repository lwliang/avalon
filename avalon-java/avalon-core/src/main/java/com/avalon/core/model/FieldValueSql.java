/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.core.model;

import com.avalon.core.field.Field;
import com.avalon.core.field.IFieldFormat;
import com.avalon.core.util.FieldValue;
import com.avalon.core.util.ObjectUtils;
import lombok.Data;

@Data
public class FieldValueSql {
    private Field field;
    private RecordColumn value;

    public void setValue(Object value) {
        this.value = new RecordColumn(value);

        if (field instanceof IFieldFormat) {
            this.value.setField((IFieldFormat) field);
        }
    }

    public void setValue(FieldValue value) {
        this.value = new RecordColumn(value);

        if (field instanceof IFieldFormat) {
            this.value.setField((IFieldFormat) field);
        }
    }

    public void setField(Field field) {
        this.field = field;
        if (ObjectUtils.isNotNull(value)) {
            value.setField((IFieldFormat) field);
        }
    }
}
