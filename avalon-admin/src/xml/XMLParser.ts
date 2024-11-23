/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

import {CharStreams, CommonTokenStream, ParseTreeWalker} from "antlr4";
import {XMLParserResult} from "./XMLParserResult.ts";
import XMLLexer from "./parser/XMLLexer.ts";
import XMLParser from "./parser/XMLParser.ts";
import {FormXMLParserVisitor} from "./parser/FormXMLParserVisitor.ts";



export async function parserEx(str: string, serviceName: string): Promise<XMLParserResult> {
    const chars = CharStreams.fromString(str);
    const lexer = new XMLLexer(chars);
    const tokens = new CommonTokenStream(lexer);
    const parser = new XMLParser(tokens);
    const tree = parser.document();

    const visitor = new FormXMLParserVisitor(serviceName)
    return await visitor.visitDocument(tree).then(data => {
        return {...data} as XMLParserResult;
    })

}