/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.core.db;

import com.avalon.core.field.Fields;
import com.avalon.core.model.Record;
import com.avalon.core.model.RecordRow;
import com.avalon.core.util.ObjectUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.jdbc.core.RowCountCallbackHandler;

import java.sql.ResultSet;
import java.sql.SQLException;

@EqualsAndHashCode(callSuper = true)
@Data
public class DataReaderRowDefaultHandler extends RowCountCallbackHandler {
    protected Record record = new Record();

    public DataReaderRowDefaultHandler() {
    }

    @Override
    protected void processRow(ResultSet rs, int rowNum) throws SQLException {
        if (ObjectUtils.isNull(record)) {
            record = new Record();
        }
        RecordRow recordRow = new RecordRow();
        for (String sqlName : getColumnNames()) {
            String fieldName = Fields.camelName(sqlName);
            Object sqlValue = rs.getObject(sqlName);
            recordRow.put(fieldName, sqlValue);
        }

        record.add(recordRow);
    }
}
