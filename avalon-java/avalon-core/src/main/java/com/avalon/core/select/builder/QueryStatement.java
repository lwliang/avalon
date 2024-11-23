/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.core.select.builder;

import com.avalon.core.model.FieldValueStatement;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QueryStatement {
    private StringBuilder sql;
    private FieldValueStatement valueStatement;
}
