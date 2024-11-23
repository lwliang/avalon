/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.core.service;

import com.avalon.core.condition.Condition;
import com.avalon.core.db.AvalonPreparedStatement;
import com.avalon.core.db.DataBaseTools;
import com.avalon.core.exception.AvalonException;
import com.avalon.core.field.Field;
import com.avalon.core.field.Fields;
import com.avalon.core.model.*;
import com.avalon.core.model.Record;
import com.avalon.core.select.builder.QueryStatement;
import com.avalon.core.select.builder.SelectBuilder;
import com.avalon.core.select.builder.SelectTreeBuilder;
import com.avalon.core.util.FieldValue;
import com.avalon.core.util.ObjectUtils;

import java.util.Optional;

public abstract class AbstractTreeService extends AbstractService implements ITreeSupport {
    public PageInfo selectTreePage(PageParam pageParam,
                                   Condition condition,
                                   String childName,
                                   String order,
                                   String... fields) throws AvalonException {

        Integer size = getContext().getApplicationConfig().getPageSize();
        if (ObjectUtils.isNotNull(pageParam.getPageSize())) {
            size = pageParam.getPageSize();
        }

        SelectBuilder selectBuilder = DataBaseTools.selectAllSql(getService(),
                fields,
                condition,
                order,
                null);

        SelectTreeBuilder selectTreeBuilder = new SelectTreeBuilder(selectBuilder, pageParam.getPageNum(), size);
        QueryStatement countSql = selectBuilder.getCountSql();
        Integer total = getJdbcTemplate().selectCount(countSql.getSql(), countSql.getValueStatement());

        QueryStatement sql = selectTreeBuilder.getSql();

        AvalonPreparedStatement avalonPrepareStatement = createAvalonPrepareStatement(sql.getSql(),
                sql.getValueStatement());
        Record record = getJdbcTemplate().select(avalonPrepareStatement, selectTreeBuilder);
        record = convertFlat2Tree(record, childName);
        return new PageInfo(record, total, pageParam.getPageNum(), size);
    }

    /**
     * 将扁平的二维数组 转换成树状结构
     *
     * @param record 需要转换的数组
     * @return 转换后的结果值
     */
    protected Record convertFlat2Tree(Record record, String childName) {
        if (ObjectUtils.isEmpty(childName)) childName = "children";
        if (ObjectUtils.isEmpty(record) ||
                !record.get(0).containsKey(getParentIdField())) return record;
        int originCount;
        do {
            originCount = record.size();
            for (int i = 0; i < record.size(); i++) {
                RecordRow row = record.get(i);
                if (row.isNull(getParentIdField())) continue;
                record.remove(i);
                i--; // 保持当前位置不变，继续遍历
                Integer parentId = row.getInteger(getParentIdField());
                Optional<RecordRow> parent = record.stream()
                        .filter(item -> item.getInteger(getPrimaryKeyField()).equals(parentId))
                        .findFirst();
                if (parent.isEmpty()) {
                    continue;
                }
                RecordRow value = parent.get();
                if (value.containsKey(childName)) {
                    value.getRecord(childName).add(row);
                } else {
                    Record children = Record.build();
                    children.add(row);
                    value.put(childName, children);
                }
            }
        } while (originCount != record.size());

        return record;
    }

    private final Field parentId = Fields.createMany2one("上级", getServiceName());

    @Override
    public Field getParentIdField() {
        return parentId;
    }

    private final Field parentPath = Fields.createString("路径"); // 格式 ,1,2,; 顶级 ,
    private final Field childIds = Fields.createOne2many("子集", this.getServiceName(), "parentId");

    @Override
    public Field getParentPathField() {
        return parentPath;
    }

    private final Field level = Fields.createInteger("层级", 1);

    @Override
    public Field getLevelField() {
        return level;
    }

    @Override
    public PrimaryKey insert(RecordRow recordRow) throws AvalonException {
        updateParentPath(recordRow);
        return super.insert(recordRow);
    }

    @Override
    public Integer update(RecordRow recordRow) throws AvalonException {
        updateParentPath(recordRow);
        return super.update(recordRow);
    }

    private void updateParentPath(RecordRow recordRow) {
        Field parentIdTemp = getParentIdField();
        Field levelTemp = getLevelField();
        Field parentPathTemp = getParentPathField();

        if (recordRow.isNull(parentIdTemp)) {
            recordRow.put(parentPathTemp, ",");
            recordRow.put(levelTemp, 1);
        } else {
            FieldValue fieldValue = getFieldValue(parentPathTemp.getName(),
                    getPrimaryKeyField().eq(recordRow.getRawValue(parentIdTemp)));
            recordRow.put(parentPathTemp, fieldValue.getString() + recordRow.getInteger(parentIdTemp) + ",");
            fieldValue = getFieldValue(levelTemp.getName(),
                    getPrimaryKeyField().eq(recordRow.get(parentIdTemp)));
            recordRow.put(levelTemp, fieldValue.getInteger() + 1);
        }
    }
}
