package com.avalon.erp.sys.addon.sale;

import com.avalon.core.module.AbstractModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author lwlianghehe@gmail.com
 * @date 2025/01/12 22:38
 */
@Service
@Slf4j
public class SaleModule extends AbstractModule {
    @Override
    public String getModuleName() {
        return "sale";
    }

    @Override
    public String getLabel() {
        return "销售";
    }

    @Override
    public String getDescription() {
        return "负责处理从潜在客户开发到最终销售的所有流程";
    }

    @Override
    public String[] depends() {
        return new String[]{
                "crm.common",
                "product"
        };
    }

    @Override
    public Boolean getDisplay() {
        return true;
    }

    @Override
    public String getIcon() {
        return "resource/sale.png";
    }

    @Override
    public String[] getResource() {
        return new String[]{
                "resource/view/sale.order.views.xml",
                "resource/view/sale.order.detail.views.xml",
                "resource/view/sale.customer.views.xml",
                "resource/view/menu.xml",
        };
    }
}
