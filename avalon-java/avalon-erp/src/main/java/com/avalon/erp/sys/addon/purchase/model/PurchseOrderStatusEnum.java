package com.avalon.erp.sys.addon.purchase.model;

import com.avalon.core.enums.ISelectFieldEnum;
import lombok.Getter;

/**
 * @author lwlianghehe@gmail.com
 * @date 2025/01/11 20:04
 */
@Getter
public enum PurchseOrderStatusEnum implements ISelectFieldEnum {
    draft("待审核"),
    approved("已审核"),
    rejected("拒绝");

    private final String name;

    PurchseOrderStatusEnum(final String name) {
        this.name = name;
    }
}
