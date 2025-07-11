package com.avalon.erp.sys.addon.product.service;

import com.avalon.core.annotation.OnChange;
import com.avalon.core.field.Field;
import com.avalon.core.field.Fields;
import com.avalon.core.model.ChangeRecordRow;
import com.avalon.core.model.RecordRow;
import com.avalon.core.service.AbstractService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author lwlianghehe@gmail.com
 * @date 2025/01/09 12:49
 */
@Service
@Slf4j
public class UnitCategoryService extends AbstractService {
    @Override
    public String getServiceName() {
        return "product.unit.category";
    }

    @Override
    public String getLabel() {
        return "产品单位类别";
    }

    @Override
    protected Field createNameField() {
        return Fields.createString("名称", true);
    }

    public Field defaultUnit = Fields.createMany2one("默认单位", "product.unit");
    public Field unitIds = Fields.createOne2many("单位", "product.unit", "unitCategoryId");

    @OnChange("name")
    public ChangeRecordRow onChangeName(RecordRow newRow, RecordRow oldRow) {
        return new ChangeRecordRow();
    }
}
