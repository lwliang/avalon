package com.avalon.erp.sys.addon.stock.service;

import com.avalon.core.field.Field;
import com.avalon.core.field.Fields;
import com.avalon.core.service.AbstractService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author lwlianghehe@gmail.com
 * @date 2025/01/11 11:58
 */
@Service
@Slf4j
public class InventoryService extends AbstractService {
    @Override
    public String getServiceName() {
        return "stock.inventory";
    }

    @Override
    public String getLabel() {
        return "产品库存表";
    }

    @Override
    public Boolean needDefaultNameField() {
        return false;
    }

    public final Field productId = Fields.createMany2one("产品", "product.product");
    public final Field locationId = Fields.createMany2one("库位", "stock.location");

    public final Field quantity = Fields.createBigDecimal("库存数量");
    public final Field reservedQuantity = Fields.createBigDecimal("已分配库存");
    public final Field availableQuantity = Fields.createBigDecimal("可用库存");//availableQuantity = quantity - reservedQuantity

    public final Field unitId = Fields.createMany2one("单位", "product.unit");
    public final Field inventoryValue = Fields.createBigDecimal("库存价值"); //库存价值（根据成本计算）
    public final Field remark = Fields.createText("备注");
}
