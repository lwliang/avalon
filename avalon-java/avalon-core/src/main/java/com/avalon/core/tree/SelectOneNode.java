/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.core.tree;

import com.avalon.core.field.*;
import com.avalon.core.service.AbstractService;
import com.avalon.core.util.FieldUtils;
import com.avalon.core.util.ObjectUtils;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

public class SelectOneNode {

    private SelectOneModel data;
    private SelectOneNodeList children;

    public SelectOneNode(SelectOneModel model) {
        super();
        this.data = model;
    }

    public SelectOneNode() {
        super();
    }

    public SelectOneModel getData() {
        return data;
    }

    public void setData(SelectOneModel data) {
        this.data = data;
    }

    public SelectOneNodeList getChildren() {
        return children;
    }

    public Boolean hasChildren() {
        return children != null && !children.isEmpty();
    }

    public void addField(String field) {

        if (ObjectUtils.isNull(data)) {
            return;
        }

        data.addField(field);
    }

    public void addChild(SelectOneNode node) {
        if (ObjectUtils.isNull(children)) {
            children = new SelectOneNodeList();
        }
        children.add(node);
    }

    /**
     * 从根节点从上往下找，同时将路径上的所有字段全部产生
     *
     * @param fieldName
     * @param service
     * @param selectOneNode 产生的节点 = this
     */
    public void addChildren(String fieldName, AbstractService service, SelectOneNode selectOneNode) {
        Integer joinCount = FieldUtils.getJoinCount(fieldName);

        if (joinCount == 0) {//可能是主表
            Field field = service.getField(fieldName);
            if (field instanceof One2oneField || field instanceof One2manyField || field instanceof Many2manyField) {
                //主表上的子表字段
                SelectOneNode node = null;
                SelectOneModel model = null;
                if (!hasChildren()) {
                    model = new SelectOneModel();
                    model.setService(((RelationField) field).getRealService());
                    node = new SelectOneNode(model);
                    selectOneNode.addChild(node);
                } else {
                    node = getChildren().get(((RelationField) field).getRealService().getServiceName());
                    if (ObjectUtils.isNull(node)) {
                        model = new SelectOneModel();
                        model.setService(((RelationField) field).getRealService());
                        node = new SelectOneNode(model);
                        selectOneNode.addChild(node);
                    } else {
                        model = node.getData();
                    }
                }
                model.setField(field);
            } else {//主表
                selectOneNode.addField(fieldName);
            }
        } else {//子表
            String tableField = FieldUtils.getJoinFirstTableString(fieldName);
            String lastField = FieldUtils.getJoinFirstFieldString(fieldName);
            Field field = service.getField(tableField);
            if (field instanceof Many2oneField) {
                selectOneNode.addField(fieldName);
            } else {
                SelectOneNode childNode = null;
                if (ObjectUtils.isNotEmpty(getChildren())) {
                    childNode = getChildren().get(((RelationField) field).getRealService().getServiceName());
                }
                if (ObjectUtils.isNull(childNode)) {
                    childNode = new SelectOneNode();
                    SelectOneModel model = new SelectOneModel();
                    model.setField(field);
                    model.setService(((RelationField) field).getRealService());
                    childNode.setData(model);
                    selectOneNode.addChild(childNode);
                    childNode.addChildren(lastField, model.getService(), childNode);//产生子表 递归
                } else {
                    childNode.addChildren(lastField, childNode.getData().getService(), childNode);
                }
            }
        }
    }
}
