/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.erp.addon.pet.model.enums;

import com.avalon.core.enums.ISelectFieldEnum;

public enum NotifyTypeEnum implements ISelectFieldEnum {
    Comment("评论"),
    Like("点赞"),
    Relay("回复");
    private final String name;

    public String getName() {
        return name;
    }

    NotifyTypeEnum(String name) {
        this.name = name;
    }
}