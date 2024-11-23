/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.erp.sys.addon.base.controller;

import com.avalon.core.condition.Condition;
import com.avalon.core.context.Context;
import com.avalon.core.context.SystemConstant;
import com.avalon.core.exception.AvalonException;
import com.avalon.core.model.Record;
import com.avalon.core.model.RecordRow;
import com.avalon.core.module.AbstractModule;
import com.avalon.core.service.AbstractService;
import com.avalon.erp.sys.addon.base.service.DBService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/db")
public class DatabaseController {
    private final Context context;

    private final DBService dbService;

    public DatabaseController(Context context, DBService dbService) {
        this.context = context;
        this.dbService = dbService;
    }

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
        return select;
    }

    @GetMapping("get/database")
    public Record getDatabases() throws AvalonException {
        String sql = String.format("select datname as data_name\n" +
                        "from pg_database\n" +
                        "WHERE datistemplate = false\n" +
                        "and datallowconn\n" +
                        "  and datname not in ('" + context.getDefaultDatabase() + "') " +
                        "  AND datdba = (select usesysid from pg_user where usename = '%s');",
                context.getApplicationConfig().getDataSource().getUsername());
        return context.getJdbcTemplate().select(sql);
    }

    @GetMapping("create/database/{database}")
    public void createDatabase(@PathVariable("database") String database) throws AvalonException {
        context.getJdbcTemplate().execute(String.format("CREATE DATABASE %s", database));
        context.init(database);
        dbService.createDataBase();
        context.invokeServiceMethod("base.module", "refreshModuleFromDisk", new ArrayList<>(), RecordRow.build());
    }

    @GetMapping("drop/database/{database}")
    public void dropDatabase(@PathVariable("database") String database) throws AvalonException {
        context.closeDB(database);
        context.init(context.getDefaultDatabase());
        context.getJdbcTemplate().execute(String.format("drop DATABASE %s", database));
    }
}
