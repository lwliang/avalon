package com.avalon.erp.sys.addon.purchase.service;

import com.avalon.core.field.Field;
import com.avalon.core.field.Fields;
import com.avalon.core.service.AbstractService;
import com.avalon.erp.sys.addon.purchase.model.GoodsReceiptStatusEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author lwlianghehe@gmail.com
 * @date 2025/01/11 18:51
 */
@Service
@Slf4j
public class GoodsReceiptNoteService extends AbstractService {
    @Override
    public String getServiceName() {
        return "purchase.goods.receipt.note";
    }

    @Override
    public String getLabel() {
        return "收货单";
    }

    @Override
    protected Field createNameField() {
        return Fields.createString("收货单号", true);
    }

    public Field orderId = Fields.createMany2one("采购订单", "purchase.order");
    public Field receiptDate = Fields.createDate("收货日期");
    public Field status = Fields.createSelection("状态", GoodsReceiptStatusEnum.class);
    public Field totalQuantity = Fields.createBigDecimal("总数量");
    public Field remark = Fields.createText("备注");
    public Field userId = Fields.createMany2one("收货人", "base.user");

    public Field detailIds = Fields.createOne2many("收货单明细",
            "purchase.goods.receipt.note.detail",
            "dfnId");
}
