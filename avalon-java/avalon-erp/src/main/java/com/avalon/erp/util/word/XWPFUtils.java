/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.erp.util.word;

import com.avalon.erp.exception.WordFileException;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xwpf.usermodel.*;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * docx文件工具类
 */
@Slf4j
@Component
public class XWPFUtils {

    private String regexText = "(\\{([^{}]+?)\\})";

    public void setRegexText(String regexText) {
        this.regexText = regexText;
    }

    /**
     * 打开docx文件
     *
     * @param fileStream 文件流
     * @return
     * @throws IOException
     */
    public static XWPFDocument openDocx(InputStream fileStream) {
        try {
            return new XWPFDocument(fileStream);
        } catch (Exception e) {
            throw new WordFileException(e.getMessage());
        }
    }

    /**
     * 保存文件
     *
     * @param doc
     * @param filePath
     * @throws IOException
     */
    public static void saveDocx(XWPFDocument doc, String filePath) {
        try {
            File file = new File(filePath);
            OutputStream outputStream = new FileOutputStream(file);
            doc.write(outputStream);
        } catch (Exception e) {
            throw new WordFileException(e.getMessage());
        }
    }

    /**
     * 写入输出流上
     *
     * @param doc
     * @param outputStream
     */
    public static void writeDocx(XWPFDocument doc, OutputStream outputStream) {
        try {
            doc.write(outputStream);
        } catch (Exception e) {
            throw new WordFileException(e.getMessage());
        }
    }

    /**
     * 获取docx文件中要替换的文本内容
     *
     * @param doc
     */
    public ParseWordNodeList iteratorParagraphAndTable(XWPFDocument doc) {
        ParseWordNodeList parseWordNodeList = new ParseWordNodeList();
        doc.getBodyElementsSpliterator().forEachRemaining(bodyElement -> {
            if (bodyElement instanceof XWPFParagraph) {
                XWPFParagraph xwpfParagraph = (XWPFParagraph) bodyElement;
                parse(parseWordNodeList, xwpfParagraph);
            } else if (bodyElement instanceof XWPFTable) {
                XWPFTable table = (XWPFTable) bodyElement;
                List<XWPFTableRow> rows = table.getRows();
                rows.forEach(row -> {
                    List<XWPFTableCell> cells = row.getTableCells();
                    cells.forEach(cell -> {
                        cell.getParagraphs().forEach(xwpfParagraph -> {
                            parse(parseWordNodeList, xwpfParagraph);
                        });
                    });
                });
            }
        });

        return parseWordNodeList;
    }

    private void parse(ParseWordNodeList parseWordNodeList, XWPFParagraph paragraph) {
        Matcher matcher = regexMatcher(paragraph.getText());
        ParseWordNode parseWordNode = new ParseWordNode();
        parseWordNode.setOrigin(paragraph.getText());
        parseWordNode.setParagraph(paragraph);
        while (matcher.find()) {
            String group = matcher.group(0);
            ParseWord parseWord = new ParseWord();
            parseWord.setWord(group);
            parseWord.setBegin(matcher.start());
            parseWord.setEnd(matcher.end());
            parseWord.setField(matcher.group(2).trim());
            parseWordNode.addParseWord(parseWord);
            parseWordNodeList.add(parseWordNode);
        }
    }

    private Matcher regexMatcher(String text) {
        Pattern compile = Pattern.compile(regexText);
        return compile.matcher(text);
    }

    /**
     * 关闭docx文件
     *
     * @param doc
     */
    public static void closeDocx(XWPFDocument doc) {
        try {
            doc.close();
        } catch (Exception e) {
            throw new WordFileException(e.getMessage());
        }
    }
}
