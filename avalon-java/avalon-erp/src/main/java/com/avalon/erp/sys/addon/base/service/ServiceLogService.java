/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.erp.sys.addon.base.service;

import com.avalon.core.field.Field;
import com.avalon.core.field.Fields;
import com.avalon.core.log.IAvalonServiceLog;
import com.avalon.core.log.ServiceLog;
import com.avalon.core.model.RecordRow;
import com.avalon.core.service.AbstractService;
import com.avalon.core.util.JacksonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@Primary
public class ServiceLogService extends AbstractService implements IAvalonServiceLog {
    // 是否启用日志
    private Boolean enableLog = true;

    @Override
    public String getServiceName() {
        return "base.service.log";
    }

    @Override
    public Boolean needDefaultNameField() {
        return false;
    }

    @Override
    public Boolean getNeedLog() {
        return false;
    }

    protected final Field handleId = Fields.createInteger("修改人");
    protected final Field handleName = Fields.createString("修改人名称");
    protected final Field serviceName = Fields.createString("服务名称");
    protected final Field serviceId = Fields.createString("主键", 20);
    protected final Field content = Fields.createText("修改内容");
    protected final Field op = Fields.createString("操作类型", 20);

    @Override
    public void insert(ServiceLog log) {
        if (!enableLog()) return;
        String s = JacksonUtil.object2String(log);
        RecordRow row = JacksonUtil.convert2Map(s);
        insert(row);
    }

    @Override
    public void update(ServiceLog log) {
        if (!enableLog()) return;
        String s = JacksonUtil.object2String(log);
        RecordRow row = JacksonUtil.convert2Map(s);
        insert(row);
    }

    @Override
    public void delete(ServiceLog log) {
        if (!enableLog()) return;
        String s = JacksonUtil.object2String(log);
        RecordRow row = JacksonUtil.convert2Map(s);
        insert(row);
    }

    @Override
    public Boolean enableLog() {
        return enableLog;
    }

    public void setEnableLog(Boolean enableLog) {
        this.enableLog = enableLog;
    }

    @Override
    public void createTable() {
        super.createTable();
    }
}
