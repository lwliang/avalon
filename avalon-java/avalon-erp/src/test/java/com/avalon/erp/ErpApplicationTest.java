/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.erp;

import com.avalon.core.context.Context;
import com.avalon.core.exception.AvalonException;
import com.avalon.erp.ErpApplication;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ErpApplication.class)
@Slf4j
public class ErpApplicationTest {
    @Autowired
    private Context context;


    @Before
    public void init() {
        context.init("avalon");
    }

    @Test
    public void testDataBase() throws AvalonException {


    }
}
