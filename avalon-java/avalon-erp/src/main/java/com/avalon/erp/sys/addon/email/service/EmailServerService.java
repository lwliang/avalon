package com.avalon.erp.sys.addon.email.service;

import com.avalon.core.field.Field;
import com.avalon.core.field.Fields;
import com.avalon.core.model.SelectionHashMap;
import com.avalon.core.service.AbstractService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author lwlianghehe@gmail.com
 * @date 2025/6/19
 */
@Service
@Slf4j
public class EmailServerService extends AbstractService {
    @Override
    public String getServiceName() {
        return "email.server";
    }

    @Override
    protected Field createNameField() {
        return Fields.createString("别名");
    }

    public Field host = Fields.createString("接收邮件服务器");
    public Field port = Fields.createInteger("接收端口号");

    public Field sendHost = Fields.createString("发送邮件服务器");
    public Field sendPort = Fields.createString("发送端口号");
    public Field protocol = Fields.createSelection("协议", new SelectionHashMap() {{
        put("ssl", "ssl协议");
        put("starttls", "starttls协议");
    }});
}
