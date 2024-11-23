/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.im.chat.controller;

import com.avalon.core.condition.Condition;
import com.avalon.core.context.Context;
import com.avalon.core.context.SystemConstant;
import com.avalon.core.exception.AvalonException;
import com.avalon.core.model.Record;
import com.avalon.core.module.AbstractModule;
import com.avalon.core.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/db")
public class DatabaseController {

    @Autowired
    private Context context;

    @GetMapping("create/table/{serviceName}")
    public String createTable(@PathVariable("serviceName") String serviceName) {
        AbstractService serviceBean = context.getServiceBean(serviceName);
        // sys.order.custom.department
        serviceBean.createTable();
        return "OK";
    }

    @GetMapping("drop/table/{serviceName}")
    public String dropTable(@PathVariable("serviceName") String serviceName) {
        AbstractService serviceBean = context.getServiceBean(serviceName);
        serviceBean.dropTable();

        return "OK";
    }

    @GetMapping("update/table/{serviceName}")
    public String updateTable(@PathVariable("serviceName") String serviceName) {
        AbstractService serviceBean = context.getServiceBean(serviceName);
        serviceBean.upgradeTable();
        return "OK";
    }

    @GetMapping("update/module/{moduleName}")
    public String updateModule(@PathVariable("moduleName") String moduleName) {
        AbstractModule base = context.getModuleMap().getModule(moduleName);
        base.upgradeModule();
        return "OK";
    }

    @GetMapping("create/module/{moduleName}")
    public String createModule(@PathVariable("moduleName") String moduleName) {
        context.setUserId(SystemConstant.ADMIN);
        AbstractModule base = context.getModuleMap().getModule(moduleName);
        base.createModule();
        return "OK";
    }

    @GetMapping("drop/module/{moduleName}")
    public String dropModule(@PathVariable("moduleName") String moduleName) {

        AbstractModule base = context.getModuleMap().getModule(moduleName);
        base.dropModule();
        return "OK";
    }

    @GetMapping("get/condition")
    public Record getCondition() throws AvalonException {
        AbstractService serviceBean = context.getServiceBean("work.engineer.type");
        Record select = serviceBean.select("id,name",
                Condition.likeCondition("name", "老板")
        );
//                        andCondition(Condition.equalCondition("password", "123")));
        return select;
    }
}
