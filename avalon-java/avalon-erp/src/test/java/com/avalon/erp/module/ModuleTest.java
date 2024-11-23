/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.erp.module;

import com.avalon.core.condition.Condition;
import com.avalon.core.context.Context;
import com.avalon.core.module.AbstractModule;
import com.avalon.erp.ErpApplication;
import com.avalon.erp.sys.addon.base.BaseModule;
import com.avalon.erp.sys.addon.base.service.ModuleService;
import com.avalon.erp.sys.addon.external.ExternalModuleAddon;
import com.avalon.erp.CustomSpringBootTestLoader;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Random;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ErpApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
@ContextConfiguration(loader = CustomSpringBootTestLoader.class)
public class ModuleTest {
    @Autowired
    private ExternalModuleAddon externalModule;
    @Autowired
    private Context context;
    @Autowired
    private BaseModule baseModule;
    @Autowired
    private ModuleService moduleService;

    @Before
    public void init() {
    }

    /**
     * 判断获取模块接口
     */
    @Test
    public void getModuleTest() {
        AbstractModule module = externalModule.getModule();
        Assertions.assertEquals(module, externalModule);
    }

    /**
     * 创建模块
     */
    @Test
    public void createModuleTest() {
        Random random = new Random();
        int id = random.nextInt(10000);
        String database = "avalon_test_" + id;
        log.info("database:{}", database);
        context.init("");
        context.setUserId(1);
        StringBuilder sql = new StringBuilder();
        sql.append("create database ").append(database);
        context.getJdbcTemplate().execute(sql);
        context.init(database);
        baseModule.createModule();
        sql.setLength(0);
        Condition condition = Condition.equalCondition("name", "base");
        Integer count = moduleService.selectCount(condition);
        Assertions.assertEquals(1, count);
    }

    @Test
    public void testLoadData() throws IOException {
        baseModule.loadData("data.xml");
    }
}
