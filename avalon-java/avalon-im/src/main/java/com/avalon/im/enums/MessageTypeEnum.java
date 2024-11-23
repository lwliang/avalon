/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.im.enums;

import com.avalon.core.enums.ISelectFieldEnum;

public enum MessageTypeEnum implements ISelectFieldEnum {
    Auth("鉴权"),
    Ack("消息确认"),
    Heartbeat("心跳"),
    Error("错误"),
    Text("文本"),
    Image("图片"),
    File("文件"),
    Video("视频"),
    Audio("音频"),
    Location("位置"),
    Link("链接"),
    Event("事件");
    private final String name;

    public String getName() {
        return this.name;
    }

    MessageTypeEnum(final String name) {
        this.name = name;
    }
}
