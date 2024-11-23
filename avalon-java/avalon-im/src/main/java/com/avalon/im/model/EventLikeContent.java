/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.im.model;

import lombok.Data;

@Data
public class EventLikeContent {
    private String likeId; // 点赞id
    private String likeUserId; // 点赞人
    private String likeUserName; // 点赞人名称
    private String likeTime; // 点赞时间
    private String userId;// 被评论人
    private String userName;// 被评论人名称
    private String fromSourceId;// 来源id
    private String fromType;// 来源类型
}
