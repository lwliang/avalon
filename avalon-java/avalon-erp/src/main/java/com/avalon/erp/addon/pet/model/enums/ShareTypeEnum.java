/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.erp.addon.pet.model.enums;

import com.avalon.core.enums.ISelectFieldEnum;

public enum ShareTypeEnum implements ISelectFieldEnum {
    share("分享"),
    help("救助");

    private final String name;

    public String getName() {
        return name;
    }

    ShareTypeEnum(String name) {
        this.name = name;
    }
}
