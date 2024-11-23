/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.erp.sys.addon.base.model.enums;

import com.avalon.core.enums.ISelectFieldEnum;

public enum PartnerTypeEnum implements ISelectFieldEnum {
    company("公司"),
    person("个人");
    private final String name;

    @Override
    public String getName() {
        return name;
    }

    PartnerTypeEnum(String name) {
        this.name = name;
    }
}
