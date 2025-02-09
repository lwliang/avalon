package com.avalon.erp.sys.addon.stock.service;

import com.avalon.core.field.Field;
import com.avalon.core.field.Fields;
import com.avalon.core.service.AbstractService;
import com.avalon.core.service.AbstractTreeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author lwlianghehe@gmail.com
 * @date 2025/01/11 11:30
 */
@Service
@Slf4j
public class StockLocationService extends AbstractTreeService {
    @Override
    public String getServiceName() {
        return "stock.location";
    }

    @Override
    public String getLabel() {
        return "库位";
    }

    @Override
    protected Field createNameField() {
        return Fields.createString("库位名称");
    }

    public final Field completeName = Fields.createString("库位完整名称");
    public final Field warehouseId = Fields.createMany2one("仓库", "stock.warehouse");
    public final Field active = Fields.createBoolean("启用", false, true);
    public final Field capacity = Fields.createBigDecimal("库存容量");

    public final Field remark = Fields.createText("备注");
}
