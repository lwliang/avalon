/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.core.table;

import com.avalon.core.alias.DefaultAliasSupport;
import com.avalon.core.condition.Condition;
import com.avalon.core.context.Context;
import com.avalon.core.db.AvalonPreparedStatement;
import com.avalon.core.enums.ServiceOperateEnum;
import com.avalon.core.exception.AvalonException;
import com.avalon.core.exception.FieldCheckException;
import com.avalon.core.field.Field;
import com.avalon.core.model.Record;
import com.avalon.core.model.*;
import com.avalon.core.service.AbstractService;
import com.avalon.core.util.FieldValue;
import com.avalon.core.util.ObjectUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Lazy;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


@Slf4j
@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
@Lazy
public abstract class AbstractTableManageService implements ITableManage, ApplicationListener<ApplicationEvent> {
    @Resource
    private Context context;

    @Override
    public ITableService getFirstTableService() {
        TableServiceList tableServices = getTableServiceList();
        if (ObjectUtils.isNull(tableServices) || tableServices.size() == 0)
            throw new FieldCheckException("未设置服务");
        return tableServices.get(0);
    }

    @Override
    public Boolean getNeedDefaultField() {
        return false;
    }

    @Override
    public Boolean getNeedDefaultKeyField() {
        return false;
    }

    @Override
    public String getBaseName() {
        return getContext().getBaseName();
    }

    @Override
    public Field getNameField() {
        ITableService firstTableService = getFirstTableService();
        return firstTableService.getNameField();
    }

    @Override
    public FieldHashMap getFieldMap() {
        ITableService firstTableService = getFirstTableService();
        return firstTableService.getFieldMap();
    }

    @Override
    public FieldHashMap getRelationFieldMap() {
        ITableService firstTableService = getFirstTableService();
        return firstTableService.getRelationFieldMap();
    }

    @Override
    public FieldHashMap getNotRelationAndPrimaryKeyFieldMap() {
        ITableService firstTableService = getFirstTableService();
        return firstTableService.getNotRelationAndPrimaryKeyFieldMap();
    }



    @Override
    public AvalonPreparedStatement createAvalonPrepareStatement(StringBuilder sql, FieldValueStatement valueStatement) {
        ITableService firstTableService = getFirstTableService();
        return firstTableService.createAvalonPrepareStatement(sql, valueStatement);
    }


    @Override
    public Integer delete(Object id) throws AvalonException {
        for (ITableService iTableService : getTableServiceList()) {
            Integer delete = iTableService.delete(id);
            if (!delete.equals(0)) return delete;
        }
        return 0;
    }

    @Override
    public Integer delete(Condition condition, String serviceName) throws AvalonException {
        Integer deleteCount = 0;
        for (ITableService iTableService : getTableServiceList()) {
            deleteCount += iTableService.delete(condition, serviceName);
        }
        return deleteCount;
    }

    @Override
    public Integer delete(RecordRow row) throws AvalonException {
        ITableService firstTableService = getFirstTableService();
        return delete(firstTableService.getPrimaryKeyValue(row));
    }

    @Override
    public Integer deleteMulti(List<Integer> ids) throws AvalonException {
        Integer deleteCount = 0;
        for (Integer id : ids) {
            for (ITableService iTableService : getTableServiceList()) {
                deleteCount += iTableService.delete(id);
            }
        }
        return deleteCount;
    }

    @Override
    public Integer deleteMulti(Record record) throws AvalonException {
        Integer deleteCount = 0;
        for (RecordRow recordRow : record) {
            deleteCount += delete(recordRow);
        }
        return deleteCount;
    }

    /**
     * 获取可以插入的服务 均衡负载
     *
     * @return recordRow
     */
    protected abstract ITableService getBalanceService(RecordRow recordRow);

    @Override
    public PrimaryKey insert(RecordRow recordRow) throws AvalonException {
        ITableService balanceService = getBalanceService(recordRow);
        return balanceService.insert(recordRow);
    }

    @Override
    public List<Object> insertMulti(Record record) throws AvalonException {

        List<Object> objs = new ArrayList<>();
        for (RecordRow recordRow : record) {
            objs.add(insert(recordRow));
        }
        return objs;
    }

    @Override
    public Integer update(RecordRow recordRow, Condition condition) throws AvalonException {
        Integer updateCount = 0;
        for (ITableService iTableService : getTableServiceList()) {
            updateCount += iTableService.update(recordRow, condition);
        }
        return updateCount;
    }

    @Override
    public Integer update(RecordRow recordRow) throws AvalonException {
        Integer updateCount = 0;
        for (ITableService iTableService : getTableServiceList()) {
            updateCount += iTableService.update(recordRow);
        }
        return updateCount;
    }

    @Override
    public Integer updateMulti(Record record) throws AvalonException {
        Integer updateCount = 0;
        for (RecordRow recordRow : record) {
            updateCount += update(recordRow);
        }
        return updateCount;
    }

    @Override
    public Object getPrimaryKeyValue(RecordRow row) {
        ITableService firstTableService = getFirstTableService();
        return firstTableService.getPrimaryKeyValue(row);
    }

    @Override
    public String getServiceName() {
        return null;
    }

    @Override
    public String getServiceTableName() {
        return null;
    }

    @Override
    public String getPrimaryKeyName() {
        ITableService firstTableService = getFirstTableService();
        return firstTableService.getPrimaryKeyName();
    }

    @Override
    public Type getPrimaryKeyType() {
        ITableService firstTableService = getFirstTableService();
        return firstTableService.getPrimaryKeyType();
    }

    @Override
    public List<String> getAllFieldName() {
        ITableService firstTableService = getFirstTableService();
        return firstTableService.getAllFieldName();
    }

    @Override
    public void createTable() {
        for (ITableService iTableService : getTableServiceList()) {
            iTableService.createTable();
        }
    }

    @Override
    public void dropTable() {
        for (ITableService iTableService : getTableServiceList()) {
            iTableService.dropTable();
        }
    }

    @Override
    public RecordRow getDefaultValueAll() {
        ITableService firstTableService = getFirstTableService();
        return firstTableService.getDefaultValueAll();
    }

    @Override
    public FieldHashMap getIsRequiredAll() {
        ITableService firstTableService = getFirstTableService();
        return firstTableService.getIsRequiredAll();
    }

    @Override
    public String getAlias(DefaultAliasSupport defaultAliasSupport) {
        return null;
    }

    @Override
    public FieldValue getSumFieldValue(Field field, Condition condition) {
        BigDecimal sum = BigDecimal.ZERO;
        for (ITableService iTableService : getTableServiceList()) {
            FieldValue sumFieldValue = iTableService.getSumFieldValue(field, condition);
            if (ObjectUtils.isNotNull(sumFieldValue) && sumFieldValue.isNotNull()) {
                sum.add(sumFieldValue.getBigDecimal());
            }
        }
        return new FieldValue(sum);
    }

    @Override
    public FieldValue getSumFieldValue(String fieldName, Condition condition) {
        BigDecimal sum = BigDecimal.ZERO;
        for (ITableService iTableService : getTableServiceList()) {
            FieldValue sumFieldValue = iTableService.getSumFieldValue(fieldName, condition);
            if (ObjectUtils.isNotNull(sumFieldValue) && sumFieldValue.isNotNull()) {
                sum.add(sumFieldValue.getBigDecimal());
            }
        }
        return new FieldValue(sum);
    }

    @Override
    public Field getPrimaryKeyField() {
        ITableService firstTableService = getFirstTableService();
        return firstTableService.getPrimaryKeyField();
    }

    @Override
    public List<Field> getFields() {
        ITableService firstTableService = getFirstTableService();
        return firstTableService.getFields();
    }

    @Override
    public Field getField(String name) {
        ITableService firstTableService = getFirstTableService();
        return firstTableService.getField(name);
    }

    @Override
    public Boolean containOp(RecordRow recordRow) {
        ITableService firstTableService = getFirstTableService();
        return firstTableService.containOp(recordRow);
    }

    @Override
    public ServiceOperateEnum getOp(RecordRow recordRow) {
        ITableService firstTableService = getFirstTableService();
        return firstTableService.getOp(recordRow);
    }

    @Override
    public AbstractService getServiceBean(String serviceName) {
        return getContext().getServiceBean(serviceName);
    }

    @Override
    public Boolean needDefaultNameField() {
        return false;
    }

    @Override
    public Context getContext() {
        return context;
    }

    @Override
    public Boolean isLazyLoadField() {
        return false;
    }

    @Override
    public FieldHashMap getUniqueFiled() {
        ITableService firstTableService = getFirstTableService();
        return firstTableService.getUniqueFiled();
    }

    @Override
    public Boolean hasRelationTable() {
        ITableService firstTableService = getFirstTableService();
        return firstTableService.hasRelationTable();
    }

    @Override
    public AbstractService getService() {
        return getFirstTableService().getService();
    }


    @Override
    public void updateNumberValue(RecordRow recordRow) {
        for (ITableService iTableService : getTableServiceList()) {
            iTableService.updateNumberValue(recordRow);
        }
    }

    @Override
    public void updateNumberValue(RecordRow recordRow, Condition condition) {
        for (ITableService iTableService : getTableServiceList()) {
            iTableService.updateNumberValue(recordRow, condition);
        }
    }


    private final TableServiceList tableServiceList = new TableServiceList();

    protected abstract void initTableService();

    @Override
    public TableServiceList getTableServiceList() {
        return tableServiceList;
    }

    @Override
    public void addTableService(ITableService tableService) {
        tableServiceList.add(tableService);
    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof ApplicationReadyEvent) {
            initTableService();
        }
    }
}
