package com.avalon.erp.sys.addon.purchase.service;

import com.avalon.core.field.Field;
import com.avalon.core.field.Fields;
import com.avalon.core.service.AbstractService;
import com.avalon.erp.sys.addon.purchase.model.PurchseOrderStatusEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author lwlianghehe@gmail.com
 * @date 2025/01/11 12:55
 */
@Service
@Slf4j
public class PurchaseOrderService extends AbstractService {
    @Override
    public String getServiceName() {
        return "purchase.order";
    }

    @Override
    public String getLabel() {
        return "采购订单";
    }

    @Override
    protected Field createNameField() {
        return Fields.createString("采购订单号", true);
    }

    public final Field orderDate = Fields.createDate("订单日期");
    public final Field expectedDate = Fields.createDate("预计到货日期");
    public final Field status = Fields.createSelection("状态", PurchseOrderStatusEnum.class);
    public Field totalAmount = Fields.createBigDecimal("采购总金额");
    public final Field userId = Fields.createMany2one("申请人", "base.user");
    public final Field remark = Fields.createText("备注");

    public Field detailIds = Fields.createOne2many("订单明细", "purchase.goods.order.detail", "orderId");
}
