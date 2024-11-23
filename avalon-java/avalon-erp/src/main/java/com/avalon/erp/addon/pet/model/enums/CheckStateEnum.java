/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.erp.addon.pet.model.enums;

import com.avalon.core.enums.ISelectFieldEnum;

public enum CheckStateEnum implements ISelectFieldEnum {
    checking("审核中"),
    fail("失败"),
    success("成功");
    private final String name;

    public String getName() {
        return name;
    }

    CheckStateEnum(String name) {
        this.name = name;
    }
}
