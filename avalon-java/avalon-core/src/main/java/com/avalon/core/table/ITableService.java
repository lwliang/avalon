/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.core.table;

import com.avalon.core.condition.Condition;
import com.avalon.core.select.builder.SelectBuilder;
import com.avalon.core.service.IAvalonService;

public interface ITableService extends IAvalonService {

    SelectBuilder selectAllRecord(String fields, Condition condition, String order);

    SelectBuilder selectAllRecord(String fields, Condition condition);

}
