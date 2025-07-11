/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.erp.sys.addon.base.service;

import com.avalon.core.condition.Condition;
import com.avalon.core.exception.AvalonException;
import com.avalon.core.field.Field;
import com.avalon.core.field.Fields;
import com.avalon.core.model.Record;
import com.avalon.core.model.RecordRow;
import com.avalon.core.model.SelectionHashMap;
import com.avalon.core.service.AbstractService;
import com.avalon.core.util.FieldUtils;
import com.avalon.core.util.StringUtils;
import com.avalon.core.util.XmlDom4jUtils;
import com.avalon.erp.sys.addon.base.model.enums.ViewModeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.xpath.*;

@Slf4j
@Service
public class ActionViewService extends AbstractService {
    @Override
    public String getServiceName() {
        return "base.action.view";
    }

    @Override
    public String getLabel() {
        return "视图";
    }

    public final Field serviceId = Fields.createMany2one("服务", "base.service");
    public final Field viewMode = Fields.createSelection("类型", ViewModeEnum.class, ViewModeEnum.tree);
    public final Field label = Fields.createString("名称");
    public final Field priority = Fields.createInteger("优先级", 16);
    public final Field moduleId = Fields.createMany2one("模块", "base.module");

    public final Field arch = Fields.createText("视图定义");
    public final Field inheritId = Fields.createMany2one("继承视图", "base.action.view");
    public final Field target = Fields.createSelection("弹出样式", new SelectionHashMap(){{
        put("new", "新窗口");
        put("current", "当前窗口");
    }});
    public final Field direction = Fields.createSelection("方向", new SelectionHashMap(){{
        put("center", "居中"); // 默认 没有值也是居中
        put("left", "左");
        put("right", "右");
        put("top", "上");
        put("bottom", "下");
    }});

    @Override
    public Integer update(RecordRow recordRow) throws AvalonException {
        return super.update(recordRow);
    }

    public Record getActionView(String fields, String order, Condition condition) {
        order = "inheritId asc nulls first, id asc";
        Record select = select(order, condition, FieldUtils.getFieldArray(fields));
        if (select.isEmpty()) {
            return select;
        }
        RecordRow firstRow = select.get(0); // 第一个页面是root页面
        String arch = firstRow.getString("arch");
        Document xmlDocument = XmlDom4jUtils.getXmlDocument(arch);
        for (int i = 1; i < select.size(); i++) { // 继承页面
            RecordRow row = select.get(i);
            String inheritArch = row.getString("arch");
            NodeList xpathElement = XmlDom4jUtils.getXpathElement(inheritArch);
            for (int j = 0; j < xpathElement.getLength(); j++) {
                Node item = xpathElement.item(j); // 来源
                if (!(item instanceof Element)) {
                    continue;
                }
                Element element = (Element) item;
                String expr = element.getAttribute("expr");
                String position = element.getAttribute("position");
                if (StringUtils.isEmpty(expr)) {
                    continue;
                }

                // 创建 XPath 工具
                XPathFactory xPathFactory = XPathFactory.newInstance();
                XPath xPath = xPathFactory.newXPath();

                // 解析 XPath 表达式并获取匹配的节点
                try {
                    XPathExpression xPathExpression = xPath.compile(expr);
                    NodeList nodeList = (NodeList) xPathExpression.evaluate(xmlDocument, XPathConstants.NODESET);
                    if (nodeList.getLength() > 0) {
                        Node item1 = nodeList.item(0);//目标
                        for (int i1 = 0; i1 < item.getChildNodes().getLength(); i1++) {
                            Node item2 = xmlDocument.importNode(item.getChildNodes().item(i1), true);
                            if (!(item2 instanceof Element)) {
                                continue;
                            }
                            if ("before".equals(position)) {
                                item1.getParentNode().insertBefore(item2, item1);
                            } else if ("after".equals(position)) {
                                item1.getParentNode().insertBefore(item2, item1.getNextSibling());
                            } else { // inside
                                item1.appendChild(item2);
                            }
                        }
                    }
                } catch (XPathExpressionException e) {
                    throw new AvalonException("xPath 识别expr错误:" + e.getMessage(), e);
                }
            }
        }
        firstRow.put("arch", XmlDom4jUtils.getXmlString(xmlDocument));
        Record build = Record.build();
        build.add(firstRow);
        return build;
    }
}
