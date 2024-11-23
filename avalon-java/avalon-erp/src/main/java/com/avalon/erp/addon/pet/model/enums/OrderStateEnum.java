/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.erp.addon.pet.model.enums;

import com.avalon.core.enums.ISelectFieldEnum;

public enum OrderStateEnum implements ISelectFieldEnum {
    wait("待付款"),
    send("待发货"),
    receive("待收货"),
    comment("待评价"),
    cancel("退单中"),
    finish("已完成");

    private final String name;

    public String getName() {
        return name;
    }

    OrderStateEnum(String name) {
        this.name = name;
    }
}
