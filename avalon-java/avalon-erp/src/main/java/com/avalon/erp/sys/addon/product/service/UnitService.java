package com.avalon.erp.sys.addon.product.service;

import com.avalon.core.field.Field;
import com.avalon.core.field.Fields;
import com.avalon.core.service.AbstractService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author lwlianghehe@gmail.com
 * @date 2025/01/09 12:50
 */
@Service
@Slf4j
public class UnitService extends AbstractService {
    @Override
    public String getServiceName() {
        return "product.unit";
    }

    @Override
    public String getLabel() {
        return "产品单位";
    }

    @Override
    protected Field createNameField() {
        return Fields.createString("编码", true);
    }

    public Field label = Fields.createString("名称");
    public Field unitCategoryId = Fields.createMany2one("单位类别", "product.unit.category");
}
