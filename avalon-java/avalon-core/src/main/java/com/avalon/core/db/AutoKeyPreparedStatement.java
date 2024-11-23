/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.core.db;

import com.avalon.core.model.FieldValueStatement;
import com.avalon.core.service.AbstractService;
import lombok.Data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * 自动返回主键
 */
public class AutoKeyPreparedStatement extends AvalonPreparedStatement {

    public AutoKeyPreparedStatement(AbstractService service, StringBuilder sql, FieldValueStatement fieldValueStatement) {
        super(service, sql, fieldValueStatement);
    }
    @Override
    protected PreparedStatement getPreparedStatement(Connection con) throws SQLException {
        return con.prepareStatement(
                getSql().toString(), new String[]{getService().getPrimaryKeyName()});
    }
}
