package com.avalon.erp.sys.addon.stock;

import com.avalon.core.module.AbstractModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author lwlianghehe@gmail.com
 * @date 2025/01/11 11:14
 */
@Service
@Slf4j
public class StockModule extends AbstractModule {
    @Override
    public String getModuleName() {
        return "stock";
    }

    @Override
    public String getLabel() {
        return "库存";
    }

    @Override
    public String getDescription() {
        return "满足灵活性、层次化和可扩展性，以支持多仓库、多库位的库存管理需求";
    }

    @Override
    public Boolean getDisplay() {
        return true;
    }

    @Override
    public String getIcon() {
        return "resource/stock.png";
    }

    @Override
    public String[] getResource() {
        return new String[]{
                "resource/view/stock.inventory.views.xml",
                "resource/view/stock.location.views.xml",
                "resource/view/stock.warehouse.views.xml",
                "resource/view/menu.xml",
        };
    }
}
