/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.core.model;

import com.avalon.core.field.Field;
import lombok.Data;

@Data
public class FieldRecord {
    private Field field;
    private Object value;

    public FieldRecord(Field field, Object value) {
        this.field = field;
        this.value = value;
    }
}
