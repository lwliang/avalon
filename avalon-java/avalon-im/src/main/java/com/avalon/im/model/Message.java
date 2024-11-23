/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.im.model;

import com.avalon.core.context.SystemConstant;
import com.avalon.core.util.DateTimeUtils;
import com.avalon.im.enums.ChatTypeEnum;
import com.avalon.im.enums.EventTypeEnum;
import com.avalon.im.enums.MessageTypeEnum;
import com.avalon.im.enums.StateEnum;
import com.avalon.im.util.MessageUtils;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class Message {
    private Integer fromUserId;
    private Integer toUserId; // 消息接收者ID Single时为用户ID Group时为群成员id
    private Integer teamId; // 群ID
    private MessageTypeEnum msgType;
    private EventTypeEnum eventType; // 事件类型 只有MsgType为Event时才有值
    private String content; // 消息内容 会根据MsgType的不同而不同
    private Long id; // 消息ID
    private Timestamp timestamp; // 消息时间戳
    private ChatTypeEnum chatType; // 聊天类型
    private StateEnum stateEnum; // 消息状态
    private Boolean isRead; // 是否已读

    public Message() {
        setId(MessageUtils.getMsgId());
        setTimestamp(DateTimeUtils.getCurrentTimestamp());
        setStateEnum(StateEnum.ServerToClientIng);
        setIsRead(false);
        setFromUserId(SystemConstant.ADMIN);
    }
}
