/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.im.model;

import lombok.Data;

@Data
public class EventCommentContent {
    private String commentId; // 评论id
    private String comment; // 评论内容
    private String commentUserId; // 评论人
    private String commentUserName; // 评论人名称
    private String commentTime; // 评论时间
    private String userId;// 被评论人
    private String userName;// 被评论人名称
    private String fromSourceId;// 来源id
    private String fromType;// 来源类型
}
