/**
 * @author lwlianghehe@gmail.com
 * @date 2024/12/16 20:33
 */
import xmlFormatter from "xml-formatter";

export function formatXML(xmlString: string): string {
    try {
        // 格式化 XML 字符串
        const formattedXML = xmlFormatter(xmlString, {
            indentation: "  ", // 使用两个空格缩进
            collapseContent: true, // 是否折叠内容（比如将单行内容保持在同一行）
            lineSeparator: "\n", // 换行符
        });
        return formattedXML;
    } catch (error) {
        // 捕获格式化错误并返回原始 XML 字符串
        console.error("Error formatting XML:", error);
        return xmlString;
    }
}


// 示例用法
// const xmlString = `<root><child>value</child><child2><nested>text</nested></child2></root>`;
// const formattedXML = formatXML(xmlString);