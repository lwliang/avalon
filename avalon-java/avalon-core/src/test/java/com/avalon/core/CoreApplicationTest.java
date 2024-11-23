/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.core;

import com.avalon.core.context.Context;
import com.avalon.core.exception.AvalonException;
import com.avalon.core.model.Record;
import com.avalon.core.model.*;
import com.avalon.core.service.AbstractService;
import com.avalon.core.util.DateTimeUtils;
import com.avalon.core.util.ObjectUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = CoreApplication.class, properties = "spring.profiles.active=dev")
@Slf4j
public class CoreApplicationTest {
    @Autowired
    private Context context;


    @Before
    public void init() {
        context.init("lwl");
    }

    @Test
    public void testGetTimeFiled() throws AvalonException {


    }


    @Test
    public void testAlias() throws AvalonException {
        AbstractService test = context.getServiceBean("test");

    }

    @Test
    public void testSelectPage() throws AvalonException {

    }

    @Test
    public void testDatetimeField() throws AvalonException {
        AbstractService test = context.getServiceBean("test");
        RecordRow recordRow = new RecordRow();
        recordRow.put("name", new RecordColumn("lwl"));
        test.insert(recordRow);
    }

    @Test
    public void testUpdateMethod() throws AvalonException {
        AbstractService test = context.getServiceBean("test");
        RecordRow recordRow = new RecordRow();
        recordRow.put("id", 3);
        recordRow.put("name", "lwl2");
        test.update(recordRow);
    }

    @Test
    public void testDeleteMethod() throws AvalonException {
        AbstractService test = context.getServiceBean("test");
        test.delete(1);
    }

    @Test
    public void testSelectionField() throws AvalonException {
        AbstractService test = context.getServiceBean("test");
        RecordRow recordRow = new RecordRow();
        recordRow.put("id", 3);
        recordRow.put("name", "lwl2");
        recordRow.put("gender", "man");
        test.insert(recordRow);
    }

    @Test
    public void testIntegerField() throws AvalonException {
        AbstractService test = context.getServiceBean("test");
        RecordRow recordRow = new RecordRow();
        recordRow.put("id", 3);
        recordRow.put("name", "integer");
        recordRow.put("gender", "man");
        recordRow.put("age", 24);
        test.insert(recordRow);
    }

    @Test
    public void testBaseField() throws AvalonException {
        AbstractService test = context.getServiceBean("test");
        RecordRow recordRow = new RecordRow();
        recordRow.put("id", 3);
        recordRow.put("name", "baseField");
        recordRow.put("gender", "man");
        recordRow.put("age", 24);
        recordRow.put("weight", 76.5);
        recordRow.put("height", 173);
        recordRow.put("salary", 18000);
        recordRow.put("birthDay", DateTimeUtils.getCurrentDate());
        test.insert(recordRow);
    }

    @Test
    public void testTimeField() throws AvalonException {
        AbstractService test = context.getServiceBean("test");
        RecordRow recordRow = new RecordRow();
        recordRow.put("name", "timeField");
        recordRow.put("wakeUp", DateTimeUtils.getCurrentTime());

        test.insert(recordRow);
    }

    @Test
    public void testCreateTable() {
        AbstractService test = context.getServiceBean("test");

        test.createTable();

    }

    @Test
    public void testDropTable() {
        AbstractService test = context.getServiceBean("test");

        test.dropTable();
    }

    @Test
    public void testUpgradeTable() {
        AbstractService test = context.getServiceBean("test");

        test.upgradeTable();
    }
}
