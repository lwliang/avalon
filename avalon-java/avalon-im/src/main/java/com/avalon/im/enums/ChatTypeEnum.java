/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.im.enums;

import com.avalon.core.enums.ISelectFieldEnum;

public enum ChatTypeEnum implements ISelectFieldEnum {
    Custom("客服"),
    Single("单聊"),
    Group("群聊"),
    Room("聊天室");
    private final String name;

    public String getName() {
        return this.name;
    }

    ChatTypeEnum(final String name) {
        this.name = name;
    }
}
