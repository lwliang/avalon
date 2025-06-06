package com.avalon.erp.sys.addon.purchase;

import com.avalon.core.module.AbstractModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * purchase_request(采购申请表) --> rfq(询价单表) --> purchase_order(采购订单表) --> grn(收货单表) --> supplier_invoice(发票表)
 * 采购申请表：记录采购需求，生成询价单。
 * 询价单表：与供应商沟通后生成采购订单。
 * 采购订单表：采购的核心单据，驱动后续流程。
 * 收货单表：确认供应商交货，并更新库存。
 * 发票表：生成供应商发票，完成付款。
 *
 * @author lwlianghehe@gmail.com
 * @date 2025/01/11 12:51
 */
@Service
@Slf4j
public class PurchaseModule extends AbstractModule {
    @Override
    public String getModuleName() {
        return "purchase";
    }

    @Override
    public String getLabel() {
        return "采购";
    }

    @Override
    public String getDescription() {
        return "采购模块是企业供应链管理中的重要部分，主要负责采购需求的计划、执行和管理";
    }

    @Override
    public Boolean getDisplay() {
        return true;
    }

    @Override
    public String[] depends() {
        return new String[]{
                "common",
                "product"
        };
    }

    @Override
    public String getIcon() {
        return "resource/purchase.png";
    }

    @Override
    public String[] getResource() {
        return new String[]{
                "resource/view/purchase.supplier.views.xml",
                "resource/view/purchase.request.views.xml",
                "resource/view/purchase.rfq.views.xml",
                "resource/view/purchase.order.views.xml",
                "resource/view/purchase.order.detail.views.xml",
                "resource/view/purchase.grn.views.xml",
                "resource/view/purchase.grn.detail.views.xml",
                "resource/view/menu.xml"
        };
    }
}
