/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.erp.sys.addon.base.model.enums;

import com.avalon.core.enums.ISelectFieldEnum;

public enum ActionTypeEnum implements ISelectFieldEnum {
    object("rpc"), // 执行服务方法
    action("action"); // 动作
    private final String name;

    ActionTypeEnum(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return "";
    }
}
