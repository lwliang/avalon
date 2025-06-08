package com.avalon.erp.sys.addon.purchase.service;

import com.avalon.core.field.Field;
import com.avalon.core.field.Fields;
import com.avalon.core.model.DelegateInheritMap;
import com.avalon.core.model.SelectionHashMap;
import com.avalon.core.service.AbstractService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author lwlianghehe@gmail.com
 * @date 2025/01/11 18:54
 */
@Service
@Slf4j
public class SupplierService extends AbstractService {
    @Override
    public DelegateInheritMap getDelegateInherit() {
        DelegateInheritMap delegateInheritMap = new DelegateInheritMap();
        delegateInheritMap.put("common.partner", "partnerId");
        return delegateInheritMap;
    }

    @Override
    public String getServiceName() {
        return "purchase.supplier";
    }

    @Override
    public String getLabel() {
        return "供应商";
    }

    public final Field partnerId = Fields.createMany2one("联系人", "common.partner");
    public Field rank = Fields.createInteger("供应商评分", 0, 5, 0);
    public Field companyType = Fields.createSelection("公司类型", new SelectionHashMap() {{
        put("company", "公司");
        put("person", "个人");
        put("group", "集团");
    }});
    public Field vat = Fields.createString("税号", true);
    public Field bankAccountNo = Fields.createString("银行账户");
    public Field remark = Fields.createText("备注");
}
