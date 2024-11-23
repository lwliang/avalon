/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.erp.util.word;

import com.avalon.core.model.RecordRow;
import com.avalon.core.service.IAvalonService;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ParseWordNodeList extends ArrayList<ParseWordNode> {

    /**
     * 获取需要查询的字段
     *
     * @return
     */
    public List<String> getFields() {
        List<String> fields = new ArrayList<>();

        this.forEach(parseWordNode -> {
            parseWordNode.getParseWordList().forEach(parseWord -> {
                if (!fields.contains(parseWord.getField())) {
                    fields.add(parseWord.getField());
                }
            });
        });

        return fields;
    }

    /**
     * 渲染值
     *
     * @param row
     */
    public void render(RecordRow row, IAvalonService service) {
        this.forEach(parseWordNode -> {
            parseWordNode.render(row, service);
        });
    }

    /**
     * 渲染值
     *
     * @param row
     * @param isReplace 是否覆盖
     */
    public void render(RecordRow row, IAvalonService service, Boolean isReplace) {
        this.forEach(parseWordNode -> {
            parseWordNode.render(row, service, isReplace);
        });
    }

    public void replace() {
        this.forEach(ParseWordNode::replace);
    }

    public void renderImage(String signFlag, InputStream inputStream) {
        this.forEach(parseWordNode -> parseWordNode.renderSignImage(signFlag, inputStream));
    }
}
