/**
 * @author lwlianghehe@gmail.com
 * @date 2025/01/11
 */

package com.avalon.erp.sys.addon.finance.service;

import com.avalon.core.field.Field;
import com.avalon.core.field.Fields;
import com.avalon.core.service.AbstractTreeService;
import com.avalon.erp.sys.addon.finance.model.enums.AccountDirectionEnum;
import com.avalon.erp.sys.addon.finance.model.enums.AccountTypeEnum;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class FinanceAccountService extends AbstractTreeService {
    @Override
    public String getServiceName() {
        return "finance.account";
    }

    @Override
    public String getLabel() {
        return "会计科目";
    }

    @Override
    protected Field createNameField() {
        return Fields.createString("科目名称", true, 100);
    }

    // 会计科目基本信息字段
    public final Field code = Fields.createString("科目编码", true, 20);
    public final Field type = Fields.createSelection("科目类型", AccountTypeEnum.class, AccountTypeEnum.asset);
    public final Field direction = Fields.createSelection("科目方向", AccountDirectionEnum.class,
            AccountDirectionEnum.debit);
    public final Field isActive = Fields.createBoolean("是否启用", false, true);
    public final Field isDetail = Fields.createBoolean("是否明细科目", false, false);
    public final Field description = Fields.createText("备注");
}