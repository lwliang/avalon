/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.im.model;

import lombok.Data;

@Data
public class EventRelayContent {
    private String relayId; // 回复id
    private String relay; // 评论内容
    private String relayUserId; // 回复人
    private String relayUserName; // 回复人名称
    private String relayTime; // 评论时间
    private String userId;// 被评论人
    private String userName;// 被评论人名称
    private String comment; // 评论内容
    private String fromSourceId;// 来源id
    private String fromType;// 来源类型
    private String parentId;// 被回复的id 及是relayId的parentId
}
