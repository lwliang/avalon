/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.erp.sys.addon.base.model.enums;

import com.avalon.core.enums.ISelectFieldEnum;

public enum ViewModeEnum implements ISelectFieldEnum {
    kanban("看板"),
    form("表单"),
    search("搜索"),
    tree("列表");
    private final String name;

    @Override
    public String getName() {
        return name;
    }

    ViewModeEnum(String name) {
        this.name = name;
    }
}