/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.core.antlr4.interpreter;


import com.avalon.core.antlr4.grammar.AvalonExprLexer;
import com.avalon.core.antlr4.grammar.AvalonExprParser;
import com.avalon.core.util.ObjectUtils;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AvalonExprManager {
    private final AvalonExprInterpreter interpreter = new AvalonExprInterpreter();

    /**
     * 计算脚本的值
     *
     * @param script 脚本 比如1+2
     * @return 返回最后一行的结果值
     */
    public Optional<Object> interpreter(String script) {
        CharStream stream = CharStreams.fromString(script);
        AvalonExprLexer lexer = new AvalonExprLexer(stream);
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        AvalonExprParser parser = new AvalonExprParser(tokenStream);

        AvalonExprParser.ProgContext tree = parser.prog();
        Object visit = interpreter.visitProg(tree);
        if (ObjectUtils.isNull(visit)) {
            return interpreter.getResult();
        }
        return Optional.ofNullable(visit);
    }

    public AvalonExprManager() {
    }

    public void putObjectValueMap(String key, Object value) {
        interpreter.addObjectValueMap(key, value);
    }

}
