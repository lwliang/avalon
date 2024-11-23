/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.core.db;

import com.avalon.core.context.Context;
import com.avalon.core.exception.AvalonException;
import com.avalon.core.exception.SQLFormatException;
import com.avalon.core.field.Field;
import com.avalon.core.field.Fields;
import com.avalon.core.field.IFieldFormat;
import com.avalon.core.model.Record;
import com.avalon.core.model.RecordRow;
import com.avalon.core.select.builder.SelectBuilder;
import com.avalon.core.tree.QueryNode;
import com.avalon.core.util.FieldUtils;
import com.avalon.core.util.FieldValue;
import com.avalon.core.util.ObjectUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.jdbc.core.RowCountCallbackHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

@EqualsAndHashCode(callSuper = true)
@Data
public class DataReaderRowHandler extends RowCountCallbackHandler {
    protected SelectBuilder selectBuilder;
    protected Record record = new Record();
    protected HashMap<String, QueryNode> queryNodeHashMap = new HashMap<>();

    public DataReaderRowHandler() {
    }

    public DataReaderRowHandler(SelectBuilder selectBuilder) {
        this.selectBuilder = selectBuilder;
    }

    @Override
    protected void processRow(ResultSet rs, int rowNum) throws SQLException {
        if (ObjectUtils.isNull(record)) {
            record = new Record();
        }
        RecordRow recordRow = new RecordRow();
        for (String sqlName : getColumnNames()) {
            String fieldName;
            String tableName = FieldUtils.getJoinFirstTableString(sqlName); // alias
            fieldName = Fields.camelName(FieldUtils.getJoinFieldString(sqlName));

            QueryNode aliasNode = null;
            String originField = tableName + FieldUtils.getJoinDivision() + fieldName;
            if (queryNodeHashMap.containsKey(tableName)) {
                aliasNode = queryNodeHashMap.get(tableName);
            } else {
                aliasNode = selectBuilder.getAliasNode(tableName);
                queryNodeHashMap.put(tableName, aliasNode);
            }
            if (ObjectUtils.isNull(aliasNode)) {
                recordRow.put(sqlName.replaceAll("\"", "").replaceAll(" ", ""),
                        rs.getObject(sqlName));
                continue;
            }
            Field field = aliasNode.getService().getField(fieldName);
            Object sqlValue = rs.getObject(sqlName);
            RecordRow resultRow = recordRow;
            if (ObjectUtils.isNull(sqlValue)) {
                resultRow.put(field, null);
                continue;
            }
            if (field instanceof IFieldFormat) {
                IFieldFormat fieldFormat = (IFieldFormat) field;
                try {
                    FieldValue fieldValue = new FieldValue(fieldFormat.parse(sqlValue));
                    fieldValue.setField(fieldFormat);
                    resultRow.put(field, fieldValue);
                } catch (AvalonException e) {
                    String msg = "字段=" + field.getName() + ",类型=" + field.getClass().getTypeName()
                            + ",格式=" + fieldFormat.getFormat() + ",值=" + sqlValue;
                    Context.getLogger(this).error(msg);
                    throw new SQLFormatException(msg);
                }
            } else {
                resultRow.put(field, sqlValue);
            }
        }

        record.add(recordRow);
    }

    /**
     * 获取记录
     *
     * @param fieldName a.b.c
     * @param result    结果
     * @return 目标结果
     */
    private RecordRow getRecordRowByFieldName(String fieldName, RecordRow result) {
        if (!FieldUtils.hasJoinSelect(fieldName)) {
            return result;
        }

        String firstField = FieldUtils.getJoinFirstTableString(fieldName);
        fieldName = FieldUtils.getJoinFirstFieldString(fieldName);

        if (result.isRecordRow(firstField)) {
            return getRecordRowByFieldName(fieldName, result.getRecordRow(firstField));
        }
        RecordRow newRecordRow = new RecordRow();
        result.put(firstField, newRecordRow);
        return getRecordRowByFieldName(fieldName, newRecordRow);
    }
}
