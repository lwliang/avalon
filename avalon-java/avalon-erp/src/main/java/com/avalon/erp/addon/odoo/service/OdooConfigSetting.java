package com.avalon.erp.addon.odoo.service;

import com.avalon.core.exception.AvalonException;
import com.avalon.core.field.Field;
import com.avalon.core.field.Fields;
import com.avalon.core.model.PrimaryKey;
import com.avalon.core.model.RecordRow;
import com.avalon.core.service.TransientService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OdooConfigSetting extends TransientService{
    @Override
    public String getServiceName() {
        return "base.config.setting";
    }

    @Override
    public String getLabel() {
        return "odoo模块设置";
    }
    @Override
    public String getInherit() {
        return "base.config.setting";
    }

    @Override
    public RecordRow create(RecordRow defaultRow) throws AvalonException {
        RecordRow initRow = super.create(defaultRow);
        initRow.put("host", invokeMethod("base.config", "getConfig", "odoo.config.host"));
        initRow.put("db",  invokeMethod("base.config", "getConfig", "odoo.config.db"));
        initRow.put("username", invokeMethod("base.config", "getConfig", "odoo.config.username"));
        initRow.put("password", invokeMethod("base.config", "getConfig", "odoo.config.password"));
        return initRow;
    }

    @Override
    public PrimaryKey insert(RecordRow recordRow) throws AvalonException {
        if(recordRow.containsKey("host")){
            invokeMethod("base.config", "setConfig", "odoo.config.host", recordRow.getString("host"));
        }
        if(recordRow.containsKey("db")){
            invokeMethod("base.config", "setConfig", "odoo.config.db", recordRow.getString("db"));
        }
        if(recordRow.containsKey("username")){
            invokeMethod("base.config", "setConfig", "odoo.config.username", recordRow.getString("username"));
        }
        if(recordRow.containsKey("password")){
            invokeMethod("base.config", "setConfig", "odoo.config.password", recordRow.getString("password"));
        }
        return super.insert(recordRow);
    }
    

    public Field host = Fields.createString("IP地址");
    public Field db = Fields.createString("数据库");
    public Field username = Fields.createString("用户名");
    public Field password = Fields.createPasswordField("密码");
}
