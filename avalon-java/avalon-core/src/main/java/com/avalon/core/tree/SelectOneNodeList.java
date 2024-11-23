/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.core.tree;

import java.util.ArrayList;

public class SelectOneNodeList extends ArrayList<SelectOneNode> {

    public SelectOneNode get(String serviceName) {
        for (SelectOneNode node : this) {
            if (node.getData().getService().getServiceName().equals(serviceName)) {
                return node;
            }
        }
        return null;
    }
}
