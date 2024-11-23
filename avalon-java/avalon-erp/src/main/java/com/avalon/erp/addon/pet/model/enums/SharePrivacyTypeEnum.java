/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.erp.addon.pet.model.enums;

import com.avalon.core.enums.ISelectFieldEnum;

public enum SharePrivacyTypeEnum implements ISelectFieldEnum {
    disClose("公开"),
    self("私有");

    private final String name;

    public String getName() {
        return name;
    }

    SharePrivacyTypeEnum(String name) {
        this.name = name;
    }
}