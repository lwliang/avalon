package com.avalon.erp.sys.addon.purchase.service;

import com.avalon.core.field.Field;
import com.avalon.core.field.Fields;
import com.avalon.core.service.AbstractService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author lwlianghehe@gmail.com
 * @date 2025/01/11 20:27
 */
@Service
@Slf4j
public class PurchaseOrderDetailService extends AbstractService {
    @Override
    public String getServiceName() {
        return "purchase.order.detail";
    }

    @Override
    public String getLabel() {
        return "采购订单明细";
    }

    @Override
    public Boolean needDefaultNameField() {
        return false;
    }

    public final Field orderId = Fields.createMany2one("采购订单", "purchase.order");
    public Field productId = Fields.createMany2one("产品", "product.product");
    public Field unitId = Fields.createMany2one("单位", "product.unit");
    public Field quantity = Fields.createBigDecimal("数量");
    public Field price = Fields.createBigDecimal("采购单价");
    public Field amount = Fields.createBigDecimal("采购金额");
    public Field supplierId = Fields.createMany2one("供应商", "purchase.supplier");
    public Field remark = Fields.createString("备注");
}
