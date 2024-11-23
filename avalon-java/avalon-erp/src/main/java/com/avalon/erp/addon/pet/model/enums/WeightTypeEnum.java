/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.erp.addon.pet.model.enums;

import com.avalon.core.enums.ISelectFieldEnum;

public enum WeightTypeEnum implements ISelectFieldEnum {
    small("小型"),
    middle("中型"),
    big("大型");

    private final String name;

    public String getName() {
        return name;
    }

    WeightTypeEnum(String name) {
        this.name = name;
    }
}
