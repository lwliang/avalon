/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.erp.sys.addon.base.controller;

import com.avalon.core.condition.Condition;
import com.avalon.core.context.Context;
import com.avalon.core.exception.AvalonException;
import com.avalon.core.exception.FieldCheckException;
import com.avalon.core.exception.RedisLockException;
import com.avalon.core.field.SelectionField;
import com.avalon.core.model.Record;
import com.avalon.core.model.*;
import com.avalon.core.redis.IRedisLock;
import com.avalon.core.redis.RedisCommon;
import com.avalon.core.service.AbstractService;
import com.avalon.core.util.FieldUtils;
import com.avalon.core.util.ObjectUtils;
import com.avalon.erp.sys.addon.base.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/model")
@Slf4j
public class ServiceController {

    @Resource
    private Context context;
    @Resource
    private RedisCommon redisCommon;

    @PostMapping("add")
    public RecordRow addModel(@RequestBody ServiceModelParam param) throws AvalonException {
        AbstractService serviceBean = context.getServiceBean(param.getServiceName());
        RecordRow recordRow = param.getValue();
        PrimaryKey id = serviceBean.insert(recordRow);
        recordRow = new RecordRow();
        recordRow.put(serviceBean.getPrimaryKeyName(), id);
        return recordRow;
    }

    @PostMapping("update")
    public RecordRow updateModel(@RequestBody ServiceModelParam param) throws AvalonException {
        AbstractService serviceBean = context.getServiceBean(param.getServiceName());
        RecordRow recordRow = param.getValue();

        String serviceName = serviceBean.getServiceName();
        serviceName = serviceName.replace(".", ":") + "update";
        serviceName += ":" + recordRow.getString(serviceBean.getPrimaryKeyName());
        IRedisLock lock = redisCommon.getLock(serviceName);
        Boolean aBoolean = lock.tryLock(10);
        Integer rows = 0;
        if (aBoolean) {
            try {
                rows = serviceBean.update(recordRow);
            } finally {
                lock.unlock();
            }

        } else {
            throw new RedisLockException();
        }

        recordRow = new RecordRow();
        recordRow.put("row", rows);
        return recordRow;
    }

    @PostMapping("delete")
    public RecordRow deleteModel(@RequestBody ServiceModelId param) throws AvalonException {
        AbstractService serviceBean = context.getServiceBean(param.getServiceName());
        Record select = serviceBean.select(Condition.equalCondition(serviceBean.getPrimaryKeyName(),
                param.getId()), serviceBean.getAllFieldName().toArray(new String[0]));
        if (select.isEmpty()) {
            throw new FieldCheckException("数据不存在,无法删除");
        }
        String serviceName = serviceBean.getServiceName();
        serviceName = serviceName.replace(".", ":") + "delete";
        serviceName += ":" + param.getId();
        IRedisLock lock = redisCommon.getLock(serviceName);
        Boolean aBoolean = lock.tryLock(10);
        Integer row = 0;
        if (aBoolean) {
            try {
                row = serviceBean.delete(select.get(0));
            } finally {
                lock.unlock();
            }
        } else {
            throw new RedisLockException();
        }


        RecordRow recordRow = RecordRow.build();
        recordRow.put("count", row);
        return recordRow;
    }

    @PostMapping("get/fields")
    public Record getModelFieldList(@RequestBody ServiceModel serviceModel) throws AvalonException {
        Record record = new Record();

        AbstractService serviceBean = context.getServiceBean(serviceModel.getServiceName());
        FieldHashMap relationFieldMap = serviceBean.getRelationFieldMap();

        serviceBean.getFieldMap().forEach((s, field) -> {
            RecordRow recordRow = new RecordRow();
            recordRow.put("label", field.getLabel());
            recordRow.put("name", field.getName());
            recordRow.put("type", field.getClassType());
            recordRow.put("isPrimaryKey", field.isPrimaryKey());
            recordRow.put("isRequired", field.isRequired());
            recordRow.put("isReadonly", field.isReadonly());
            recordRow.put("isAutoIncrement", field.isAutoIncrement());
            recordRow.put("isUnique", field.isUnique());
            recordRow.put("allowNull", field.allowNull());
            recordRow.put("defaultValue", null);
            if (ObjectUtils.isNotNull(field.getDefaultValue())) {
                recordRow.put("defaultValue", field.getDefaultValue().getDefaultString());
            }
            recordRow.put("isExternalField", field.getIsExternalField());
            record.add(recordRow);
        });

        return record;
    }

    /**
     * description: 获取serviceBean 里面的 selection 值，没有做传值判断
     * version: 1.0
     * date: 2022/4/8 9:13
     * author: AN
     *
     * @param param
     * @return com.avalon.core.model.RecordRow
     */
    @PostMapping("get/selection/map")
    public RecordRow getSelection(@RequestBody ServiceModelField param) {
        AbstractService serviceBean = context.getServiceBean(param.getServiceName());
        RecordRow row = new RecordRow();

        SelectionField selectionField = (SelectionField) serviceBean.getField(param.getFields());
        Record record = new Record();
        selectionField.getSection().forEach(((o, s) -> {
            RecordRow row1 = new RecordRow();
            row1.put("id", o);
            row1.put("name", s);
            record.add(row1);
        }));

        row.put("data", record);
        return row;
    }


    @PostMapping("get/detail")
    public RecordRow getDetail(@RequestBody ServiceModelField param) throws AvalonException {
        AbstractService serviceBean = context.getServiceBean(param.getServiceName());
        Condition condition = Condition.parseRPN(param.getRpnCondition());

        Record select = serviceBean.select(condition, FieldUtils.getFieldArray(param.getFields()));

        if (select.isEmpty()) {
            return null;
        }
        return select.get(0);
    }


    /**
     * 获取全部数据
     *
     * @param serviceConditionPage
     * @return
     */
    @PostMapping("get/all")
    public Record getAll(@RequestBody ServiceModelField serviceConditionPage) {
        AbstractService serviceBean = context.getServiceBean(serviceConditionPage.getServiceName());

        return serviceBean.select(serviceConditionPage.getOrder(),
                Condition.parseRPN(serviceConditionPage.getRpnCondition()),
                FieldUtils.getFieldArray(serviceConditionPage.getFields()));
    }


    @PostMapping("/get/page")
    public PageInfo getPage(@RequestBody ServiceModelPage serviceModelPage) throws AvalonException {
        AbstractService serviceBean = context.getServiceBean(serviceModelPage.getServiceName());
        if (ObjectUtils.isNull(serviceModelPage.getIsDistinct())) {
            serviceModelPage.setIsDistinct(true);
        }
        return serviceBean.selectPage(serviceModelPage.getPage(),
                serviceModelPage.getOrder(),
                Condition.parseRPN(serviceModelPage.getRpnCondition()),
                FieldUtils.getFieldArray(serviceModelPage.getFields()));
    }

}
