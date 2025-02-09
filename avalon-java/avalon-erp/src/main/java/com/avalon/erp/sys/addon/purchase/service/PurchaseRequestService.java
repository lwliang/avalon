package com.avalon.erp.sys.addon.purchase.service;

import com.avalon.core.field.Field;
import com.avalon.core.field.Fields;
import com.avalon.core.service.AbstractService;
import com.avalon.erp.sys.addon.purchase.model.PurchseRequestStatusEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author lwlianghehe@gmail.com
 * @date 2025/01/11 12:50
 */

@Service
@Slf4j
public class PurchaseRequestService extends AbstractService {
    @Override
    public String getServiceName() {
        return "purchase.request";
    }

    @Override
    public String getLabel() {
        return "采购申请单";
    }

    @Override
    protected Field createNameField() {
        return Fields.createString("采购申请单号", true);
    }

    public final Field requestDate = Fields.createDate("申请日期");
    public final Field status = Fields.createSelection("状态", PurchseRequestStatusEnum.class);
    public final Field userId = Fields.createMany2one("申请人", "base.user");
    public final Field productId = Fields.createMany2one("产品", "product.product");
    public final Field unitId = Fields.createMany2one("单位", "product.unit");
    public final Field quantity = Fields.createBigDecimal("申请数量");
    public final Field remark = Fields.createText("备注");

    public Field rfqIds = Fields.createOne2many("询价单", "purchase.request.for.quotation", "requestId");
}
