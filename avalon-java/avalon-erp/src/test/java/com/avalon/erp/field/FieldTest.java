/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.erp.field;

import com.avalon.core.condition.Condition;
import com.avalon.core.context.Context;
import com.avalon.erp.ErpApplication;
import com.avalon.erp.CustomSpringBootTestLoader;
import com.avalon.erp.field.service.FieldServiceDomain;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Random;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ErpApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
@ContextConfiguration(loader = CustomSpringBootTestLoader.class)
public class FieldTest {
    @Autowired
    private Context context;
    @Autowired
    private FieldServiceDomain fieldServiceDomain;

    @Test
    public void testFieldBuilder() {
        fieldServiceDomain.createTable();
        fieldServiceDomain.select(Condition.likeCondition("firstName", ""));
    }

    @Before
    public void init() {
        createDatabase();
    }

    private void createDatabase() {
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
    }
}
