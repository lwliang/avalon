package com.avalon.core.model;

import com.avalon.core.field.Fields;
import com.avalon.core.field.Many2manyField;
import com.avalon.core.service.AbstractService;
import com.avalon.core.service.ExternalService;
import org.bouncycastle.tsp.ers.SortedHashList;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * @author lwlianghehe@gmail.com
 * @date 2025/05/14 22:44
 */
public class Man2ManyServiceRecordRow {
    private final List<Many2manyField> many2manyFieldList = new ArrayList<>();

    public void putMany2manyService(Many2manyField field) {
        many2manyFieldList.add(field);
    }

    public AbstractService getMany2manyService(String many2manyServiceName) {
        Optional<Many2manyField> first = many2manyFieldList.stream().
                filter(x -> Fields.dot2UnderscoreName(many2manyServiceName).equals(x.getTableSqlName())).findFirst();

        return first.isPresent() ? first.get().getRealService() : null;
    }
}
