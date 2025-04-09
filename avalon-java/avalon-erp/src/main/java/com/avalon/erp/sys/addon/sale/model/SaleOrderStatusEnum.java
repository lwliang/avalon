package com.avalon.erp.sys.addon.sale.model;

import com.avalon.core.enums.ISelectFieldEnum;
import lombok.Getter;

/**
 * @author lwlianghehe@gmail.com
 * @date 2025/01/11 20:04
 */
@Getter
public enum SaleOrderStatusEnum implements ISelectFieldEnum {
    draft("报价单未发送"),
    sent("报价单已发送"),
    sale("销售订单"),
    done("销售完成"),
    cancel("销售已取消");

    private final String name;

    SaleOrderStatusEnum(final String name) {
        this.name = name;
    }
}
