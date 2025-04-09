package com.avalon.erp.sys.addon.sale.service;

import com.avalon.core.field.Field;
import com.avalon.core.field.Fields;
import com.avalon.core.service.AbstractService;
import com.avalon.erp.sys.addon.sale.model.SaleOrderStatusEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author lwlianghehe@gmail.com
 * @date 2025/01/12 22:41
 */

/**
 * 报价单与销售订单
 */
@Service
@Slf4j
public class SaleOrderDetailService extends AbstractService {
    @Override
    public String getServiceName() {
        return "sale.order.detail";
    }

    @Override
    public String getLabel() {
        return "销售订单明细";
    }

    @Override
    public Boolean needDefaultNameField() {
        return false;
    }

    public Field orderId = Fields.createMany2one("销售订单", "sale.order");
    public Field productId = Fields.createMany2one("产品", "product.product");
    public Field unitId = Fields.createMany2one("单位", "product.unit");
    public Field quantity = Fields.createBigDecimal("数量");
    public Field price = Fields.createBigDecimal("销售单价");
    public Field amount = Fields.createBigDecimal("销售金额");
    public Field remark = Fields.createString("备注");

}
