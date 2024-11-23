/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.core.model;

import com.avalon.core.util.FieldValue;

/**
 * 主键值
 */
public class PrimaryKey extends FieldValue {
    public PrimaryKey(Object value) {
        super(value);
    }
    public PrimaryKey(FieldValue value) {
        super(value.getValue());
    }

    public static PrimaryKey build(Object key) {
        return new PrimaryKey(key);
    }
    public static PrimaryKey build(FieldValue key) {
        return new PrimaryKey(key);
    }
}
