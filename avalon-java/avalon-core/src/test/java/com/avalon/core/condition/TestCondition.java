/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.core.condition;

import com.avalon.core.CoreApplication;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.assertEquals;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = CoreApplication.class, properties = "spring.profiles.active=dev")
@Slf4j
public class TestCondition {
    @Test
    public void testEqualCondition() {
        Condition condition = Condition.equalCondition("name", 1);
        String reversePolishNotation = condition.getReversePolishNotation();

        assertEquals("(=,name,1)", reversePolishNotation);
    }

    @Test
    public void testBetweenCondition() {
        Condition condition = Condition.betweenCondition("name", 1, 2);
        String reversePolishNotation = condition.getReversePolishNotation();

        assertEquals("(between,name,1,2)", reversePolishNotation);
    }

    @Test
    public void testInCondition() {
        Condition condition = Condition.inCondition("name", 1, 2, 3);
        String reversePolishNotation = condition.getReversePolishNotation();

        assertEquals("(in,name,1,2,3)", reversePolishNotation);
    }

    @Test
    public void testSpanCondition() {
        Condition condition = Condition.subGreaterThanSpanCondition("name", 1, 2);
        String reversePolishNotation = condition.getReversePolishNotation();

        assertEquals("(span,name,1,2)", reversePolishNotation);
    }


    @Test
    public void testAndCondition() {
        Condition condition = Condition.equalCondition("name", 1);
        condition = condition.andCondition(Condition.lessCondition("id", 2));

        assertEquals("(&,(=,name,1),(<,id,2))", condition.getReversePolishNotation());
    }

    @Test
    public void testOrCondition() {
        Condition condition = Condition.equalCondition("name", 1);
        condition = condition.orCondition(Condition.lessCondition("id", 2));

        assertEquals("(|,(=,name,1),(<,id,2))", condition.getReversePolishNotation());
    }

    @Test
    public void testRPNEqual() {
        String con = "(=,name,1)";
        Condition condition = Condition.parseRPN(con);
        String reversePolishNotation = condition.getReversePolishNotation();

        assertEquals(con, reversePolishNotation);
    }

    @Test
    public void testRPNLess() {
        String con = "(<,name,1)";
        Condition condition = Condition.parseRPN(con);
        String reversePolishNotation = condition.getReversePolishNotation();

        assertEquals(con, reversePolishNotation);
    }

    @Test
    public void testRPNLessEqual() {
        String con = "(<=,name,1)";
        Condition condition = Condition.parseRPN(con);
        String reversePolishNotation = condition.getReversePolishNotation();

        assertEquals(con, reversePolishNotation);
    }

    @Test
    public void testRPNBetween() {
        String con = "(between,name,1,2)";
        Condition condition = Condition.parseRPN(con);
        String reversePolishNotation = condition.getReversePolishNotation();

        assertEquals(con, reversePolishNotation);
    }

    @Test
    public void testRPNIn() {
        String con = "(in,name,1,2,5)";
        Condition condition = Condition.parseRPN(con);
        String reversePolishNotation = condition.getReversePolishNotation();

        assertEquals(con, reversePolishNotation);
    }

    @Test
    public void testRPNAnd() {
        String con = "(&,(=,name,1),(<,id,5))";
        Condition condition = Condition.parseRPN(con);
        String reversePolishNotation = condition.getReversePolishNotation();

        assertEquals(con, reversePolishNotation);
    }

    @Test
    public void testRPNOr() {
        String con = "(|,(=,name,1),(<,id,5))";
        Condition condition = Condition.parseRPN(con);
        String reversePolishNotation = condition.getReversePolishNotation();

        assertEquals(con, reversePolishNotation);
    }


    @Test
    public void testRPNAndOr() {
        String con = "(&,(|,(=,name,1),(<,id,5)),(=,name,1))";
        Condition condition = Condition.parseRPN(con);
        String reversePolishNotation = condition.getReversePolishNotation();

        assertEquals(con, reversePolishNotation);
    }

    @Test
    public void testRPNOrAnd() {
        String con = "(|,(&,(=,name,1),(<,id,5)),(=,name,1))";
        Condition condition = Condition.parseRPN(con);
        String reversePolishNotation = condition.getReversePolishNotation();

        assertEquals(con, reversePolishNotation);
    }
}
