/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.core.tree;

import lombok.Data;

import java.util.ArrayList;

@Data
public class QueryNodeList extends ArrayList<QueryNode> {
    public QueryNode getNodeByTable(String tableName) {
        for (QueryNode queryNode : this) {
            if (queryNode.getTableField().equals(tableName)) {
                return queryNode;
            }
        }
        return null;
    }
}
