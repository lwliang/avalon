package com.avalon.erp.sys.addon.finance.model.enums;

import com.avalon.core.enums.ISelectFieldEnum;

public enum AccountTypeEnum implements ISelectFieldEnum {
    asset("资产"), // 资产类 借增贷减
    liability("负债"), // 负债类 贷增借减
    equity("所有者权益"), // 所有者权益类 贷增借减
    revenue("收入"), // 收入类 贷增借减
    expense("费用"); // 费用类 借增贷减

    private final String name;

    AccountTypeEnum(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
} 