/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.core.field;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FieldList extends ArrayList<Field> {

    public Field find(String name) {
        List<Field> collect = this.stream().filter(x -> x.getName().equals(name)).collect(Collectors.toList());
        if(collect.isEmpty()) {
            return null;
        }
        return collect.get(0);
    }
}
