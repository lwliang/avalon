/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.core.field;

import com.avalon.core.face.field.IFieldDefaultValue;
import com.avalon.core.service.AbstractService;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 所有关系字段的基类
 */
public abstract class RelationField extends Field {
    public RelationField(Builder builder) {
        super(builder);
    }

    public RelationField(String label) {
        super(label);
    }

    public RelationField(String label, Boolean isRequired) {
        super(label, isRequired);
    }

    public RelationField(String label,
                         Boolean isRequired,
                         Boolean isReadonly) {
        super(label, isRequired, isReadonly);
    }

    public RelationField(String label,
                         Boolean isRequired,
                         Boolean isReadonly,
                         Boolean allowNull,
                         IFieldDefaultValue defaultValue) {
        super(label, isRequired, isReadonly, defaultValue);
        setAllowNull(allowNull);
    }

    public RelationField(String label, Boolean isRequired,
                         Boolean isReadonly,
                         Boolean allowNull,
                         IFieldDefaultValue defaultValue,
                         Boolean isPrimaryKey, Boolean isAutoIncrement) {
        super(label, isRequired, isReadonly, defaultValue, isPrimaryKey, isAutoIncrement);
        setAllowNull(allowNull);
    }

    public abstract String getRelativeServiceName();


    public abstract String getRelativeFieldName();
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public abstract AbstractService getRelativeService();

    public AbstractService getRealService() {
        return getRelativeService();
    }
}
