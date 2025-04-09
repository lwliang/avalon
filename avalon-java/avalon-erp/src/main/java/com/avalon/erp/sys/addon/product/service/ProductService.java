package com.avalon.erp.sys.addon.product.service;

import com.avalon.core.field.Field;
import com.avalon.core.field.Fields;
import com.avalon.core.service.AbstractService;
import com.avalon.erp.sys.addon.product.model.ProductTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author lwlianghehe@gmail.com
 * @date 2025/01/09 12:46
 */
@Service
@Slf4j
public class ProductService extends AbstractService {
    @Override
    public String getServiceName() {
        return "product.product";
    }

    @Override
    public String getLabel() {
        return "产品";
    }

    @Override
    protected Field createNameField() {
        return Fields.createString("产品名称", true);
    }

    public Field image = Fields.createImage("图片");
    public Field code = Fields.createString("产品编码", true);
    public Field description = Fields.createText("描述");
    public Field unitId = Fields.createMany2one("单位", "product.unit");
    public Field productType = Fields.createSelection("产品类型", ProductTypeEnum.class);
}
