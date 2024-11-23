/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.erp.addon.pet.model.enums;

import com.avalon.core.enums.ISelectFieldEnum;

public enum RemindOftenEnum implements ISelectFieldEnum {
    once("一次"),
    day("每天"),
    week("每周"),
    twoWeek("每两周"),
    threeWeek("每三周"),
    month("每月"),
    twoMonth("每两月"),
    threeMonth("每三月"),
    year("每年");

    private final String name;

    public String getName() {
        return name;
    }

    RemindOftenEnum(String name) {
        this.name = name;
    }
}