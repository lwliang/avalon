package com.avalon.erp.sys.addon.purchase.model;

import com.avalon.core.enums.ISelectFieldEnum;
import lombok.Getter;

/**
 * @author lwlianghehe@gmail.com
 * @date 2025/01/11 21:50
 */
@Getter
public enum GoodsReceiptStatusEnum implements ISelectFieldEnum {
    draft("待审核"),
    approved("已审核"),
    rejected("拒绝");

    private final String name;

    GoodsReceiptStatusEnum(final String name) {
        this.name = name;
    }
}
