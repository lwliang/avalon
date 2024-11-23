/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.erp.util.word;

import com.avalon.core.model.RecordRow;
import com.avalon.core.service.IAvalonService;
import com.avalon.core.util.ObjectUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.Document;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Data
@Slf4j
public class ParseWordNode {

    private String origin;//原始

    private final List<ParseWord> parseWordList = new ArrayList<>();
    private XWPFParagraph paragraph;
    private String renderValue = null;

    public void addParseWord(ParseWord parseWord) {
        parseWordList.add(parseWord);
    }

    public void addParseWord(String parseWord, int begin, int end) {
        ParseWord word = new ParseWord();
        word.setWord(parseWord);
        word.setBegin(begin);
        word.setEnd(end);
        parseWordList.add(word);
    }

    /**
     * 渲染数据 默认马上覆盖
     *
     * @param row
     */
    public void render(RecordRow row, IAvalonService service) {
        render(row, service, true);
    }


    /**
     * 渲染数据
     *
     * @param row
     * @param service
     * @param isReplace 是否马上替换
     */
    public void render(RecordRow row, IAvalonService service, Boolean isReplace) {
        doRender(row, service);

        if (isReplace) {
            replace();
        }
    }

    /**
     * 渲染单个节点
     *
     * @param row
     * @param service
     * @param parseWord
     */
    public void renderNode(RecordRow row, IAvalonService service, ParseWord parseWord) {
        if (ObjectUtils.isNull(renderValue)) {
            renderValue = origin;
        }

        String value = "";

        String field = parseWord.getField();
        if (!row.containsKey(field)) {//不存在 则跳过
            return;
        }

        if (ObjectUtils.isNull(row.get(field))) {
            //存在并且必须为null才能覆盖
            renderValue = renderValue.replace(parseWord.getWord(), value);
            return;
        }

        if (!parseWord.getValue(row, service).isEmpty()) {
            value = parseWord.getValue(row, service).getString();
        }
        renderValue = renderValue.replace(parseWord.getWord(), value);

    }


    /**
     * 覆盖原数据
     */
    public void replace() {
        String color;
        String fontName;
        Double fontSize;
        List<XWPFRun> runs = paragraph.getRuns();

        color = runs.get(0).getColor();
        fontName = runs.get(0).getFontFamily();
        fontSize = runs.get(0).getFontSizeAsDouble();

        log.debug("color:{}", color);
        log.debug("fontName:{}", fontName);
        log.debug("fontSize:{}", fontSize);

        while (runs.size() > 0) {//去除所有的值
            paragraph.removeRun(0);
        }
        XWPFRun run = paragraph.createRun();

        run.setText(renderValue);
        if (color != null) {
            run.setColor(color);
        }
        run.setFontFamily(fontName);
        if (fontSize != null) {
            run.setFontSize(fontSize);
        }
        paragraph.addRun(run);
    }

    public void renderSignImage(String signFlag, InputStream inputStream) {
        List<XWPFRun> runs = paragraph.getRuns();
        if (runs != null) {

            for (XWPFRun r : runs) {
                String flagStr = r.getText(r.getTextPosition());

                if (flagStr.contains(signFlag)) {

                    String color;
                    String fontName;
                    Double fontSize;

                    color = runs.get(0).getColor();
                    fontName = runs.get(0).getFontFamily();
                    fontSize = runs.get(0).getFontSizeAsDouble();

                    String[] t = flagStr.split(signFlag, 2);

                    XWPFRun run = paragraph.createRun();
                    run.setText(t[0]);
                    run.setColor(color);
                    run.setFontFamily(fontName);
                    run.setFontSize(fontSize);

                    XWPFRun run2 = paragraph.createRun();
                    try {
                        run2.addPicture(
                                inputStream,
                                Document.PICTURE_TYPE_JPEG,
                                "img",
                                Units.toEMU(50),
                                Units.toEMU(50)
                        );
                    } catch (Exception e) {
                        System.out.println("替换错误");
                        System.out.println(e.getMessage());
                    }

                    XWPFRun run3 = paragraph.createRun();
                    run3.setText(t[1]);
                    run3.setColor(color);
                    run3.setFontFamily(fontName);
                    run3.setFontSize(fontSize);

                    r.setText("", 0);//删除
                    break;
                }

            }
        }
    }

    private void doRender(RecordRow row, IAvalonService service) {
        if (ObjectUtils.isNull(renderValue)) {
            renderValue = origin;
        }
        for (ParseWord parseWord : parseWordList) {
            String value = "";

            String field = parseWord.getField();
            if (!row.containsKey(field)) {//不存在 则跳过
                continue;
            }

            if (ObjectUtils.isNull(row.get(field))) {
                //存在并且必须为null才能覆盖
                renderValue = renderValue.replace(parseWord.getWord(), value);
                continue;
            }

            if (!parseWord.getValue(row, service).isEmpty()) {
                value = parseWord.getValue(row, service).getString();
            }
            renderValue = renderValue.replace(parseWord.getWord(), value);
        }
    }
}
