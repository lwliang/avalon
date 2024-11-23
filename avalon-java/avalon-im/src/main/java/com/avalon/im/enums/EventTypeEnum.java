/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.im.enums;

import com.avalon.core.enums.ISelectFieldEnum;

public enum EventTypeEnum implements ISelectFieldEnum {
    OfflineMessage("离线消息"),
    Comment("评论"),
    Like("点赞"),
    Relay("回复"),
    Share("分享"), // 关注的人 发分享则推送
    Follow("关注"),
    Unfollow("取消关注"),
    Offline("离线"),
    Online("上线");
    private final String name;

    public String getName() {
        return name;
    }

    EventTypeEnum(final String name) {
        this.name = name;
    }
}
