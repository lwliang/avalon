package com.avalon.erp.sys.addon.finance.model.enums;

import com.avalon.core.enums.ISelectFieldEnum;

// 账套类型枚举
 public enum AccountSetTypeEnum implements ISelectFieldEnum{
    COMPANY("公司账套"),
    PROJECT("项目账套"),
    DEPARTMENT("部门账套");
    
    private final String name;
    
    AccountSetTypeEnum(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}