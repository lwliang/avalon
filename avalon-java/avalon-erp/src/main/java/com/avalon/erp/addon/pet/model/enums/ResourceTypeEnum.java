/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.erp.addon.pet.model.enums;

import com.avalon.core.enums.ISelectFieldEnum;

public enum ResourceTypeEnum implements ISelectFieldEnum {
    carousel("轮播图"),
    activity("活动"),
    meBg("背景"),
    share("分享"),
    other("其他"),
    orderState("订单状态");

    private final String name;

    @Override
    public String getName() {
        return name;
    }

    ResourceTypeEnum(String name) {
        this.name = name;
    }
}
