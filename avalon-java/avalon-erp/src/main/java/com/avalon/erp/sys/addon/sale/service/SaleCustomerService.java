package com.avalon.erp.sys.addon.sale.service;

import com.avalon.core.field.Field;
import com.avalon.core.field.Fields;
import com.avalon.core.model.DelegateInheritMap;
import com.avalon.core.model.SelectionHashMap;
import com.avalon.core.service.AbstractService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author lwlianghehe@gmail.com
 * @date 2025/01/13 8:46
 */
@Service
@Slf4j
public class SaleCustomerService extends AbstractService {
    @Override
    public String getServiceName() {
        return "sale.customer";
    }

    @Override
    public String getLabel() {
        return "客户";
    }

    @Override
    public Boolean needDefaultNameField() {
        return false;
    }

    @Override
    public DelegateInheritMap getDelegateInherit() {
        DelegateInheritMap delegateInheritMap = new DelegateInheritMap();
        delegateInheritMap.put("crm.partner", "partnerId");
        return delegateInheritMap;
    }

    public Field partnerId = Fields.createMany2one("联系人", "crm.partner");
    public Field rank = Fields.createInteger("排名", 0, 5);// 客户排名 标识客户的活跃度
    public Field companyType = Fields.createSelection("公司类型", new SelectionHashMap() {{
        put("company", "公司");
        put("person", "个人");
        put("group", "集团");
    }});
}
