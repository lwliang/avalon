/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.core.antlr;

import com.avalon.core.CoreApplication;
import com.avalon.core.TestCustomContextLoader;
import com.avalon.core.antlr4.condition.ConditionInterpreter;
import com.avalon.core.antlr4.condition.ExpressionLexer;
import com.avalon.core.antlr4.condition.ExpressionParser;
import com.avalon.core.antlr4.interpreter.AvalonExprManager;
import com.avalon.core.condition.Condition;
import lombok.extern.slf4j.Slf4j;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = CoreApplication.class, properties = "spring.profiles.active=dev", webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ContextConfiguration(loader = TestCustomContextLoader.class)
@Slf4j
public class AvalonExprTest {

    @Autowired
    private AvalonExprManager avalonExprManager;

    @Test
    public void testVar() {
        Optional<Object> value = avalonExprManager.interpreter("a=1;");

        assertEquals(value.get(), new BigDecimal(1));
    }

    @Test
    public void testMul() {
        Optional<Object> value = avalonExprManager.interpreter("a=1;a=1*2;a=a*3;");

        assertEquals(value.get(), new BigDecimal(6));
    }

    @Test
    public void testDiv() {
        Optional<Object> value = avalonExprManager.interpreter("a=1;a=1*2;a=2/a;");

        assertEquals(value.get(), new BigDecimal(1));
    }

    @Test
    public void testAdd() {
        Optional<Object> value = avalonExprManager.interpreter("a=1;a=a+2;");

        assertEquals(value.get(), new BigDecimal(3));
    }

    @Test
    public void testSub() {
        Optional<Object> value = avalonExprManager.interpreter("a=1;a=a-2;");

        assertEquals(value.get(), new BigDecimal(-1));
    }

    @Test
    public void testUnary() {
        Optional<Object> value = avalonExprManager.interpreter("a=-1;");

        assertEquals(value.get(), new BigDecimal(-1));
    }

    @Test
    public void testEq() {
        Optional<Object> value = avalonExprManager.interpreter("a=1;b=1;c=a==b;");

        assertEquals(value.get(), true);
    }

    @Test
    public void testNEq() {
        Optional<Object> value = avalonExprManager.interpreter("a=1;b=1;c=a!=b;");

        assertEquals(value.get(), false);
    }

    @Test
    public void testGT() {
        Optional<Object> value = avalonExprManager.interpreter("1>2;");

        assertEquals(value.get(), false);
    }

    @Test
    public void testIf() {
        Optional<Object> value = avalonExprManager.interpreter("if (2>1){a=1+2;}");
        assertEquals(value.get(), BigDecimal.valueOf(3));
    }

    @Test
    public void testIfElse() {
        Optional<Object> value = avalonExprManager.interpreter("if (1>2){a=1+2;}else{b=2+2;b=b+2;}");
        assertEquals(value.get(), BigDecimal.valueOf(6));
    }

    @Test
    public void testIfElseIf() {
        Optional<Object> value = avalonExprManager.interpreter("if (1>2)" +
                "{ " + System.lineSeparator() + "a=1+2;}else if (2==2){b=2+2;b=b+2;}");
        assertEquals(value.get(), BigDecimal.valueOf(6));
    }

    @Test
    public void testFor() {
        Optional<Object> value = avalonExprManager.interpreter("b=0;for(a=1;a<10;a++){b++;} a;");
        assertEquals(value.get(), BigDecimal.valueOf(10));
    }


    @Test
    public void testWhile() {
        Optional<Object> value = avalonExprManager.interpreter("b=0;a=0;while(a<10){a++;b++;} a;");
        assertEquals(value.get(), BigDecimal.valueOf(10));
    }

    @Test
    public void testObjectField() {
        Optional<Object> value = avalonExprManager.interpreter("a.a;");
        assertEquals(value.get(), 10);
    }

    @Test
    public void testObjectMethodWithNoArgs() {
        Optional<Object> value = avalonExprManager.interpreter("a.sayHello();");
        assertEquals(value.get(), "Hello,World");
    }

    @Test
    public void testObjectMethodWithArgs() {
        Optional<Object> value = avalonExprManager.interpreter("a.add(1,2);");
        assertEquals(value.get(), BigDecimal.valueOf(3));
    }

    @Test
    public void testConditionEq() {
        CharStream stream = CharStreams.fromString("('a',=,1)");
        ExpressionLexer lexer = new ExpressionLexer(stream);
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        ExpressionParser parser = new ExpressionParser(tokenStream);

        ExpressionParser.ExprContext tree = parser.expr();
        ConditionInterpreter interpreter = new ConditionInterpreter();
        Condition visit = interpreter.visitExpr(tree);
        assertEquals("( a = 1)", visit.toString());
    }

    @Test
    public void testConditionIdentifierEq() {
        CharStream stream = CharStreams.fromString("('a',=,b)");
        ExpressionLexer lexer = new ExpressionLexer(stream);
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        ExpressionParser parser = new ExpressionParser(tokenStream);

        ExpressionParser.ExprContext tree = parser.expr();
        ConditionInterpreter interpreter = new ConditionInterpreter();
        Condition visit = interpreter.visitExpr(tree);
        assertEquals("( a = b)", visit.toString());
    }

    @Test
    public void testConditionAnd() {
        CharStream stream = CharStreams.fromString("('a',=,1)&('b',>,'x')");
        ExpressionLexer lexer = new ExpressionLexer(stream);
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        ExpressionParser parser = new ExpressionParser(tokenStream);

        ExpressionParser.ExprContext tree = parser.expr();
        ConditionInterpreter interpreter = new ConditionInterpreter();
        Condition visit = interpreter.visitExpr(tree);
        assertEquals("(( a = 1) AND (b > 'x'))", visit.toString());
    }

    @Test
    public void testConditionOr() {
        CharStream stream = CharStreams.fromString("('a',=,1)|('b',>,'x')");
        ExpressionLexer lexer = new ExpressionLexer(stream);
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        ExpressionParser parser = new ExpressionParser(tokenStream);

        ExpressionParser.ExprContext tree = parser.expr();
        ConditionInterpreter interpreter = new ConditionInterpreter();
        Condition visit = interpreter.visitExpr(tree);
        assertEquals("( ( a = 1) OR (b > 'x'))", visit.toString());
    }

    @Test
    public void testConditionNot() {
        CharStream stream = CharStreams.fromString("!('a',=,1)");
        ExpressionLexer lexer = new ExpressionLexer(stream);
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        ExpressionParser parser = new ExpressionParser(tokenStream);

        ExpressionParser.ExprContext tree = parser.expr();
        ConditionInterpreter interpreter = new ConditionInterpreter();
        Condition visit = interpreter.visitExpr(tree);
        assertEquals("( NOT ( a = 1))", visit.toString());
    }

    @Test
    public void testConditionNotAnd() {
        CharStream stream = CharStreams.fromString("!(('a',=,1)&('b',>,'x'))");
        ExpressionLexer lexer = new ExpressionLexer(stream);
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        ExpressionParser parser = new ExpressionParser(tokenStream);

        ExpressionParser.ExprContext tree = parser.expr();
        ConditionInterpreter interpreter = new ConditionInterpreter();
        Condition visit = interpreter.visitExpr(tree);
        assertEquals("( NOT (( a = 1) AND (b > 'x')))", visit.toString());
    }

    @Test
    public void testConditionIn() {
        CharStream stream = CharStreams.fromString("('a',in,(1,2,3,4))");
        ExpressionLexer lexer = new ExpressionLexer(stream);
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        ExpressionParser parser = new ExpressionParser(tokenStream);

        ExpressionParser.ExprContext tree = parser.expr();
        ConditionInterpreter interpreter = new ConditionInterpreter();
        Condition visit = interpreter.visitExpr(tree);
        assertEquals("(a IN (1234))", visit.toString());
    }

    @Test
    public void testConditionNotIn() {
        CharStream stream = CharStreams.fromString("!('a',in,(1,2,3,4))");
        ExpressionLexer lexer = new ExpressionLexer(stream);
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        ExpressionParser parser = new ExpressionParser(tokenStream);

        ExpressionParser.ExprContext tree = parser.expr();
        ConditionInterpreter interpreter = new ConditionInterpreter();
        Condition visit = interpreter.visitExpr(tree);
        assertEquals("( NOT (a IN (1234)))", visit.toString());
    }

    @Test
    public void testConditionBetween() {
        CharStream stream = CharStreams.fromString("('a',between,1,2)");
        ExpressionLexer lexer = new ExpressionLexer(stream);
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        ExpressionParser parser = new ExpressionParser(tokenStream);

        ExpressionParser.ExprContext tree = parser.expr();
        ConditionInterpreter interpreter = new ConditionInterpreter();
        Condition visit = interpreter.visitExpr(tree);
        assertEquals("(a BETWEEN 1 AND 2)", visit.toString());
    }
}
