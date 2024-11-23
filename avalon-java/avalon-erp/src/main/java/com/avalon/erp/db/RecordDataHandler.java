/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.erp.db;

import com.avalon.core.model.Record;
import com.avalon.core.model.RecordRow;
import com.avalon.core.util.ObjectUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.jdbc.core.RowCountCallbackHandler;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * description: RecordDataHandler
 * date: 2022/5/23 17:49
 * author: AN
 * version: 1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class RecordDataHandler extends RowCountCallbackHandler {

    private Record record = new Record();

    public RecordDataHandler() {
    }

    @Override
    protected void processRow(ResultSet rs, int rowNum) throws SQLException {

        if (ObjectUtils.isNull(record)) {
            record = new Record();
        }
        RecordRow recordRow = new RecordRow();
        for (String sqlName : getColumnNames()) {
            recordRow.put(sqlName, rs.getString(sqlName));
        }

        record.add(recordRow);

    }
}
