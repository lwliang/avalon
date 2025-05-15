package com.avalon.core.antlr4.condition;

import com.avalon.core.condition.Condition;
import com.avalon.core.context.Context;
import com.avalon.core.util.ObjectUtils;
import com.avalon.core.util.StringUtils;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.springframework.stereotype.Component;

/**
 * @author lwlianghehe@gmail.com
 * @date 2025/02/13 10:58
 */
@Component
public class ConditionManager {
    private ConditionInterpreter interpreter = new ConditionInterpreter();

    public Condition interpreter(Context context, String script) {
        if (StringUtils.isEmpty(script)) {
            return null;
        }
        interpreter.setContext(context);
        CharStream stream = CharStreams.fromString(script);
        ExpressionLexer lexer = new ExpressionLexer(stream);
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        ExpressionParser parser = new ExpressionParser(tokenStream);

        ExpressionParser.ExprContext tree = parser.expr();

        return interpreter.visitExpr(tree);
    }
}
