/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.erp.addon.pet.model.enums;

import com.avalon.core.enums.ISelectFieldEnum;

public enum UserSexEnum implements ISelectFieldEnum {
    man("男"),
    female("女");

    private final String name;

    public String getName() {
        return name;
    }

    UserSexEnum(String name) {
        this.name = name;
    }
}
