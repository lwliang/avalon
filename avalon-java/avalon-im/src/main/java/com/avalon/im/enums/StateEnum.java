/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.im.enums;

import com.avalon.core.enums.ISelectFieldEnum;

public enum StateEnum implements ISelectFieldEnum {
    ClientToServerIng("客户端->服务端"), //  客户端发生给服务器，服务器未完成接收
    Server("服务端"), //  服务端已接收
    ServerToClientIng("服务端->客户端"), //  服务器发生给客户端，客户端未完成接收
    Client("客户端"); //  客户端已接收
    private final String name;

    public String getName() {
        return this.name;
    }

    StateEnum(final String name) {
        this.name = name;
    }
}
