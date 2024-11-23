/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.erp.sys.addon.base.controller;

import com.avalon.core.condition.Condition;
import com.avalon.core.context.Context;
import com.avalon.core.exception.AvalonException;
import com.avalon.core.exception.FieldCheckException;
import com.avalon.core.field.SelectionField;
import com.avalon.core.model.FieldHashMap;
import com.avalon.core.model.PageInfo;
import com.avalon.core.model.Record;
import com.avalon.core.model.RecordRow;
import com.avalon.core.service.AbstractService;
import com.avalon.core.util.FieldUtils;
import com.avalon.core.util.ObjectUtils;
import com.avalon.core.util.RecordRowUtils;
import com.avalon.core.util.StringUtils;
import com.avalon.erp.sys.addon.base.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/service")
public class ServiceV2Controller {

    @Autowired
    private Context context;

    @PostMapping("{serviceName}/add")
    public RecordRow addModel(@PathVariable("serviceName") String serviceName,
                              @RequestBody ServiceModelParam param) throws AvalonException {
        AbstractService serviceBean = context.getServiceBean(serviceName);
        RecordRow recordRow = param.getValue();

        return RecordRow.build().put(serviceBean.getPrimaryKeyName(), serviceBean.insert(recordRow));
    }

    @PostMapping("{serviceName}/update")
    public RecordRow updateModel(@PathVariable("serviceName") String serviceName,
                                 @RequestBody ServiceModelParam param) throws AvalonException {
        AbstractService serviceBean = context.getServiceBean(serviceName);
        RecordRow recordRow = param.getValue();

        return RecordRow.build().put("count", serviceBean.update(recordRow));
    }

    @PostMapping("{serviceName}/delete")
    public Integer deleteModel(@PathVariable("serviceName") String serviceName,
                               @RequestBody ServiceModelId param) throws AvalonException {
        AbstractService serviceBean = context.getServiceBean(serviceName);
        Record select = serviceBean.select(
                Condition.equalCondition(serviceBean.getPrimaryKeyName(), param.getId()),
                serviceBean.getAllFieldName().toArray(new String[0]));

        if (select.isEmpty()) {
            throw new FieldCheckException("数据不存在,无法删除");
        }
        return serviceBean.delete(select.get(0));
    }


    @PostMapping("get/{serviceName}/detail")
    public RecordRow getDetail(@PathVariable("serviceName") String serviceName,
                               @RequestBody ServiceModelField param) throws AvalonException {
        AbstractService serviceBean = context.getServiceBean(serviceName);
        Condition condition = Condition.parseRPN(param.getRpnCondition());
        Record select = serviceBean.select(condition, FieldUtils.getFieldArray(param.getFields()));
        if (select.isEmpty()) {
            return RecordRow.build();
        }
        return select.get(0);
    }


    /**
     * 获取全部数据
     *
     * @param serviceConditionPage
     * @return
     */
    @PostMapping("get/{serviceName}/all")
    public Record getAll(@PathVariable("serviceName") String serviceName,
                         @RequestBody ServiceModelField serviceConditionPage) {
        AbstractService serviceBean = context.getServiceBean(serviceName);

        return serviceBean.select(serviceConditionPage.getOrder(),
                Condition.parseRPN(serviceConditionPage.getRpnCondition()),
                FieldUtils.getFieldList(serviceConditionPage.getFields()).toArray(new String[0]));
    }

    @PostMapping("/get/{serviceName}/page")
    public PageInfo getPage(@PathVariable("serviceName") String serviceName,
                            @RequestBody ServiceModelPage serviceModelPage) throws AvalonException {
        AbstractService serviceBean = context.getServiceBean(serviceName);

        return serviceBean.selectPage(serviceModelPage.getPage(),
                serviceModelPage.getOrder(),
                Condition.parseRPN(serviceModelPage.getRpnCondition()),
                FieldUtils.getFieldList(serviceModelPage.getFields()).toArray(new String[0]));
    }

    @PostMapping("get/{serviceName}/fields")
    public Record getModelFieldList(@PathVariable("serviceName") String serviceName,
                                    @RequestBody ServiceModel serviceModel) throws AvalonException {
        AbstractService serviceBean = context.getServiceBean("base.field");
        String fields = "id,label,name,isPrimaryKey,isAutoIncrement,isRequired,isReadonly,defaultValue," +
                "type,serviceId,isUnique,allowNull,minValue,maxValue,masterForeignKeyName,relativeForeignKeyName," +
                "relativeServiceName,manyServiceTable,relativeFieldName";
        return serviceBean.select(Condition.equalCondition("serviceId.name", serviceName),
                FieldUtils.getFieldArray(fields));
    }

    /**
     * description: 获取serviceBean 里面的 selection 值，没有做传值判断
     *
     * @param param
     * @return com.avalon.core.model.RecordRow
     */
    @PostMapping("get/{serviceName}/selection/map")
    public RecordRow getSelection(@PathVariable("serviceName") String serviceName,
                                  @RequestBody ServiceModelField param) {
        AbstractService serviceBean = context.getServiceBean(serviceName);
        RecordRow row = new RecordRow();

        SelectionField selectionField = (SelectionField) serviceBean.getField(param.getFields());
        selectionField.getSection().forEach(((o, s) -> {
            row.put(o.toString(), s);
        }));

        return row;
    }

    @PostMapping("invoke/{serviceName}/method")
    public Object invokeService(@PathVariable("serviceName") String serviceName,
                                @RequestBody ServiceInvokeParam param) throws AvalonException {
        AbstractService serviceBean = context.getServiceBean(serviceName);
        return serviceBean.invokeMethod(param.getMethod(), param.getIds(), param.getParam());
    }
}
