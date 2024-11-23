/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.core.condition.field;

import com.avalon.core.condition.Condition;
import com.avalon.core.field.Field;

public interface INotEqualCondition {
    Condition ne(Object value);
}
