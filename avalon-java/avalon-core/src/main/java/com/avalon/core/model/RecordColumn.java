/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.core.model;

import com.avalon.core.field.IFieldFormat;
import com.avalon.core.util.FieldValue;
import com.avalon.core.util.ObjectUtils;

import java.io.Serializable;

public class RecordColumn extends FieldValue implements Serializable, IFieldFormat {
    public RecordColumn(Object value) {
        super(value);
    }

    public RecordColumn(FieldValue value) {
        super(ObjectUtils.isNotNull(value) ? value.getValue() : null);
        if (ObjectUtils.isNotNull(value)) {
            setField(value.getField());
        }
    }
}
