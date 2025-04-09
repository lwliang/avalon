/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.core.condition;

import com.avalon.core.CoreApplication;
import com.avalon.core.TestCustomContextLoader;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.assertEquals;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = CoreApplication.class, properties = "spring.profiles.active=dev", webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ContextConfiguration(loader = TestCustomContextLoader.class)
@Slf4j
public class TestCondition {
    @Test
    public void testEqualCondition() {
        Condition condition = Condition.equalCondition("name", 1);
        String conditionString = condition.getConditionString();

        assertEquals("('name',=,1)", conditionString);
    }
}
