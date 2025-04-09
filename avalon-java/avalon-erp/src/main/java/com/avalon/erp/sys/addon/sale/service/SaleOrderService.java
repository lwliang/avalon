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
public class SaleOrderService extends AbstractService {
    @Override
    public String getServiceName() {
        return "sale.order";
    }

    @Override
    public String getLabel() {
        return "销售订单";
    }

    @Override
    protected Field createNameField() {
        return Fields.createString("销售订单号", true);
    }

    public final Field orderDate = Fields.createDate("订单日期");
    public final Field expectedDate = Fields.createDate("预计发货日期");
    public final Field status = Fields.createSelection("状态", SaleOrderStatusEnum.class, SaleOrderStatusEnum.draft);
    public Field totalAmount = Fields.createBigDecimal("销售总金额");
    public Field userId = Fields.createMany2one("销售人员", "base.user");
    public Field detailIds = Fields.createOne2many("订单明细", "sale.order.detail", "orderId");
}
