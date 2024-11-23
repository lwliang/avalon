/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.core.spel;

import com.avalon.core.CoreApplication;
import com.avalon.core.context.Context;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CoreApplication.class, properties = "spring.profiles.active=dev")
@Slf4j
public class SpelTest {
    @Autowired
    private Context context;

    @Test
    public void testStringLiteral() {
        String script = "'hello world'.getBytes()";
        Object o = context.executeScript(script);
        log.info("{}", o);
    }

    @Test
    public void testDoubleLiteral() {
        String script = "1.2";
        Object o = context.executeScript(script);
        log.info("{}", o);
    }

    @Test
    public void testBooleanLiteral() {
        String script = "true";
        Object o = context.executeScript(script);
        log.info("{}", o);

        script = "false";
        o = context.executeScript(script);
        log.info("{}", o);
    }

    @Test
    public void testEvaluationContext() {

    }

    @Test
    public void testArrayLiteral() {
        String script = "{1,2,3,'4'}";
        Object o = context.executeScript(script);
        log.info("{}", o);
    }

    @Test
    public void testListLiteral() {
        String script = "T(java.util.Arrays).asList(1,2,3,4)";
        Object o = context.executeScript(script);
        log.info("{}", o);
    }

    @Test
    public void testFunction() {
        String script = "getCurrentDate()";
        Object o = context.executeScript(script);
        log.info("{}", o);
    }

    @Data
    private class Simple {
        private Integer id;
    }
}
