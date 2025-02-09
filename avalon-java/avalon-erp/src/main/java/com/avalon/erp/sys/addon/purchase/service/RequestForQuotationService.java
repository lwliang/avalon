package com.avalon.erp.sys.addon.purchase.service;

import com.avalon.core.field.Field;
import com.avalon.core.field.Fields;
import com.avalon.core.service.AbstractService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author lwlianghehe@gmail.com
 * @date 2025/01/11 21:09
 */
@Service
@Slf4j
public class RequestForQuotationService extends AbstractService {
    @Override
    public String getServiceName() {
        return "purchase.request.for.quotation";
    }

    @Override
    public String getLabel() {
        return "询价单";
    }

    @Override
    protected Field createNameField() {
        return Fields.createString("询价单号", true);
    }

    public Field requestId = Fields.createMany2one("申请单", "purchase.request");
    public Field productId = Fields.createMany2one("产品", "product.product");
    public Field unitId = Fields.createMany2one("单位", "product.unit");
    public Field quantity = Fields.createBigDecimal("询价数量");
    public Field price = Fields.createBigDecimal("供应商报价");
    public Field supplierId = Fields.createMany2one("供应商", "purchase.supplier");
    public Field deadline = Fields.createDate("报价截至时间");
    public Field use = Fields.createBoolean("使用");
    public Field remark = Fields.createString("备注");
}
