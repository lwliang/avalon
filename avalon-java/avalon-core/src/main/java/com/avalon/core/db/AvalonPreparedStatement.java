/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.core.db;

import com.avalon.core.field.*;
import com.avalon.core.model.FieldValueSql;
import com.avalon.core.model.FieldValueStatement;
import com.avalon.core.model.RecordColumn;
import com.avalon.core.service.AbstractService;
import com.avalon.core.util.FieldUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.PreparedStatementCreator;

import java.sql.*;

@Slf4j
public class AvalonPreparedStatement implements PreparedStatementCreator {

    private AbstractService service;
    private StringBuilder sql;
    private FieldValueStatement fieldValueStatement;

    public AvalonPreparedStatement(AbstractService service,
                                   StringBuilder sql,
                                   FieldValueStatement fieldValueStatement) {
        this.service = service;
        this.sql = sql;
        this.fieldValueStatement = fieldValueStatement;
    }

    @Override
    public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
        PreparedStatement ps = getPreparedStatement(con);

        int index = 1;
        for (FieldValueSql fieldValueSql : fieldValueStatement) {
            Field field = fieldValueSql.getField();
            RecordColumn value = fieldValueSql.getValue();

            if (value.isEmpty()) {
                if (FieldUtils.isString(field)) {
                    ps.setString(index, "");
                } else {
                    ps.setNull(index, field.getSqlType());
                }
                index++;
                continue;
            }

            if (field instanceof ImageField || field instanceof StringField || field instanceof SelectionField) {
                ps.setString(index, value.getString());
            } else if (field instanceof BooleanField) {
                ps.setBoolean(index, value.getBoolean());
            } else if (field instanceof IntegerField) {
                ps.setInt(index, value.getInteger());
            } else if (field instanceof FloatField) {
                ps.setFloat(index, value.getFloat());
            } else if (field instanceof DoubleField) {
                ps.setDouble(index, value.getDouble());
            } else if (field instanceof BigDecimalField) {
                ps.setBigDecimal(index, value.getBigDecimal());
            } else if (field instanceof BigIntegerField) {
                ps.setLong(index, value.getLong());
            } else if (field instanceof DateField ||
                    field instanceof DateTimeField ||
                    field instanceof TimeField) {
                if (this.service.getContext().isMysql()) {
                    ps.setString(index, field.getSqlValue(value.getValue()).toString());
                } else {
                    if (field instanceof TimeField) {
                        ps.setTime(index, new Time(value.getDate().getTime()));
                    } else if (field instanceof DateField) {
                        ps.setDate(index, new Date(value.getDate().getTime()));
                    } else {
                        ps.setTimestamp(index, new Timestamp(value.getDate().getTime()));
                    }
                }
            } else if (field instanceof TimestampField) {
                ps.setTimestamp(index, value.getTimestamp());
            } else if (field instanceof Many2oneField || field instanceof One2oneField) {
                try {
                    AbstractService realService = ((RelationField) field).getRealService();
                    Field primaryKeyField = realService.getPrimaryKeyField();
                    if (primaryKeyField instanceof IntegerField) {
                        ps.setInt(index, value.getInteger());
                    } else {
                        ps.setLong(index, value.getLong());
                    }
                }catch (Exception ex) {
                    log.error(ex.getMessage(), ex);
                    ps.setInt(index, value.getInteger());
                }
            }
            index++;
        }

        return ps;
    }

    protected PreparedStatement getPreparedStatement(Connection con) throws SQLException {
        PreparedStatement ps = con.prepareStatement(
                sql.toString());

        return ps;
    }

    @Override
    public String toString() {
        return getSql().toString() + ";" + getParam();
    }

    private String getParam() {
        StringBuilder builder = new StringBuilder();
        builder.append("sql param->");
        fieldValueStatement.forEach((fieldValueSql) -> {
            builder.append(fieldValueSql.getValue()).append(",");
        });
        return builder.toString();
    }

    public AbstractService getService() {
        return service;
    }

    public StringBuilder getSql() {
        return sql;
    }

    public FieldValueStatement getFieldValueStatement() {
        return fieldValueStatement;
    }
}
