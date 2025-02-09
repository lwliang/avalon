package com.avalon.erp.sys.addon.product.model;

import com.avalon.core.enums.ISelectFieldEnum;

/**
 * @author lwlianghehe@gmail.com
 * @date 2025/01/09 20:51
 */
public enum ProductTypeEnum implements ISelectFieldEnum {
    consumable("可消耗"),
    stock("可库存"),
    service("服务"); // 动作
    private final String name;

    ProductTypeEnum(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}
