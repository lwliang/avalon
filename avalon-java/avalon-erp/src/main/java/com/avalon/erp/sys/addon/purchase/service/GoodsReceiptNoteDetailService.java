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
public class GoodsReceiptNoteDetailService extends AbstractService {
    @Override
    public String getServiceName() {
        return "purchase.goods.receipt.note.detail";
    }

    @Override
    public String getLabel() {
        return "收货单";
    }

    @Override
    public Boolean needDefaultNameField() {
        return false;
    }

    public Field gfnId = Fields.createMany2one("收货单", "purchase.goods.receipt.note");
    public Field orderDetailId = Fields.createMany2one("采购订单明细", "purchase.order.detail");
    public Field receivableQuantity = Fields.createBigDecimal("应收数量");
    public Field receivedQuantity = Fields.createBigDecimal("已收数量");
    public Field unreceivedQuantity = Fields.createBigDecimal("未收数量");
    public Field remark = Fields.createString("备注");
}
