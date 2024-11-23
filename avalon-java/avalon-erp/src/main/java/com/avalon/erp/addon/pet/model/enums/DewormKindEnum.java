/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.erp.addon.pet.model.enums;

import com.avalon.core.enums.ISelectFieldEnum;

public enum DewormKindEnum implements ISelectFieldEnum {
    external("体外"),
    internal("体内");
    private final String name;

    public String getName() {
        return name;
    }

    DewormKindEnum(String name) {
        this.name = name;
    }
}
