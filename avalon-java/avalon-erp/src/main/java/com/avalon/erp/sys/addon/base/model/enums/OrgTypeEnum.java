/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.erp.sys.addon.base.model.enums;

import com.avalon.core.enums.ISelectFieldEnum;

public enum OrgTypeEnum implements ISelectFieldEnum {
    company("公司"),
    department("部门");
    private final String name;

    @Override
    public String getName() {
        return name;
    }

    OrgTypeEnum(String name) {
        this.name = name;
    }
}
