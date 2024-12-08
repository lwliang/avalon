package com.avalon.erp.sys.addon.base.model.enums;

import com.avalon.core.enums.ISelectFieldEnum;

/**
 * @author lwlianghehe@gmail.com
 * @date 2024/12/01 19:25
 */
public enum FieldSourceTypeEnum implements ISelectFieldEnum {
    system("系统"),
    custom("自定义");
    private final String name;

    @Override
    public String getName() {
        return name;
    }

    FieldSourceTypeEnum(String name) {
        this.name = name;
    }
}