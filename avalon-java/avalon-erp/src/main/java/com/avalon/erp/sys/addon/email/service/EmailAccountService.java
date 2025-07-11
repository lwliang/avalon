package com.avalon.erp.sys.addon.email.service;

import com.avalon.core.condition.Condition;
import com.avalon.core.exception.AvalonException;
import com.avalon.core.field.Field;
import com.avalon.core.field.Fields;
import com.avalon.core.model.Record;
import com.avalon.core.model.RecordRow;
import com.avalon.core.service.AbstractService;
import com.avalon.core.util.DynamicMailUtil;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author lwlianghehe@gmail.com
 * @date 2025/6/19
 */
@Service
@Slf4j
public class EmailAccountService extends AbstractService {
    @Override
    public String getServiceName() {
        return "email.account";
    }

    @Override
    public String getLabel() {
        return "邮箱账户";
    }

    @Override
    protected Field createNameField() {
        return Fields.createString("账号");
    }

    public Field password = Fields.createString("password");
    public Field emailServerId = Fields.createMany2one("服务", "email.server");
    public Field userId = Fields.createMany2one("用户", "base.user");

    public RecordRow testConnect(RecordRow param) {
         String name = param.getString("name");
         String password = param.getString("password");
         Integer serverId = param.getInteger("emailServerId");
         AbstractService emailServerService = getServiceBean("email.server");
         Record select = emailServerService.select(Condition.equalCondition("id", serverId),
         "sendHost", "sendPort", "protocol");
         if(select.isEmpty()) {
            throw new AvalonException("未设置邮件服务器");
         }
         RecordRow server = select.get(0);
         DynamicMailUtil.connect(server.getString("sendHost"), 
         server.getInteger("sendPort"), 
         name, 
         password, 
         server.getString("protocol").equals("ssl"));

         RecordRow row = RecordRow.build();
         RecordRow returnParam = RecordRow.build();
         returnParam.put("title", "邮件");
         returnParam.put("message", "连接成功");
         row.put("type", "ir.actions.client")
                 .put("tag", "notifyAction")
                 .put("param", returnParam);
         return row;
    }
}
