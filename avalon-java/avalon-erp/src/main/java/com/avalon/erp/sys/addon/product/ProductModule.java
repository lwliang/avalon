package com.avalon.erp.sys.addon.product;

import com.avalon.core.module.AbstractModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author lwlianghehe@gmail.com
 * @date 2025/01/08 22:52
 */
@Service
@Slf4j
public class ProductModule extends AbstractModule {
    @Override
    public String getModuleName() {
        return "product";
    }

    @Override
    public String getLabel() {
        return "产品";
    }

    @Override
    public String getDescription() {
        return "产品信息 、 产品价格、单位";
    }

    @Override
    public Boolean getDisplay() {
        return true;
    }

    @Override
    public String getIcon() {
        return "resource/product.png";
    }

    @Override
    public String[] getResource() {
        return new String[]{
                "resource/view/product.unit.views.xml",
                "resource/view/product.unit.category.views.xml",
                "resource/view/product.views.xml",
                "resource/record/product.unit.category.xml",
                "resource/record/product.unit.xml",
                "resource/view/menu.xml",
        };
    }
}
