/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.erp.addon.pet.model.enums;

import com.avalon.core.enums.ISelectFieldEnum;

public enum PetSexEnum implements ISelectFieldEnum {
    man("哥哥"),
    female("妹妹");
    private final String name;

    public String getName() {
        return name;
    }

    PetSexEnum(String name) {
        this.name = name;
    }
}
