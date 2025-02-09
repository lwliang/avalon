package com.avalon.erp.sys.addon.purchase.model;

import com.avalon.core.enums.ISelectFieldEnum;

/**
 * @author lwlianghehe@gmail.com
 * @date 2025/01/11 20:04
 */
public enum PurchseRequestStatusEnum implements ISelectFieldEnum {
    draft("待审核"),
    approved("已审核"),
    rejected("拒绝");

    private final String name;

    public String getName() {
        return name;
    }

    PurchseRequestStatusEnum(final String name) {
        this.name = name;
    }
}
