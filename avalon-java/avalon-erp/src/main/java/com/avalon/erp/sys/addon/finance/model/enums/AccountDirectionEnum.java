package com.avalon.erp.sys.addon.finance.model.enums;

import com.avalon.core.enums.ISelectFieldEnum;

public enum AccountDirectionEnum implements ISelectFieldEnum {
    debit("借方"),
    credit( "贷方");

    private final String name;

    AccountDirectionEnum(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}
