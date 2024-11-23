/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.im.util;

import com.avalon.core.util.JacksonUtil;
import com.avalon.core.util.SnowflakeIdWorker4rd;
import com.avalon.im.enums.ChatTypeEnum;
import com.avalon.im.enums.EventTypeEnum;
import com.avalon.im.enums.MessageTypeEnum;
import com.avalon.im.model.AckContent;
import com.avalon.im.model.Message;

public class MessageUtils {
    /**
     * 获取消息ID
     *
     * @return Long
     */
    public static Long getMsgId() {
        return SnowflakeIdWorker4rd.nextUId();
    }

    /**
     * 创建离线消息
     *
     * @return Message
     */
    public static Message createOfflineMessage(Integer toUserId, int count) {
        Message message = new Message();
        message.setToUserId(toUserId);
        message.setMsgType(MessageTypeEnum.Event);
        message.setEventType(EventTypeEnum.OfflineMessage);
        message.setChatType(ChatTypeEnum.Single);
        message.setContent(String.format("您有%d离线消息", count));
        return message;
    }

    /**
     * 创建ACK消息
     *
     * @param toUserId 接收人
     * @param srcId    源消息ID
     * @return Message
     */
    public static Message createAckMessage(Integer toUserId, Long srcId) {
        Message message = new Message();
        message.setToUserId(toUserId);
        message.setMsgType(MessageTypeEnum.Ack);
        message.setChatType(ChatTypeEnum.Single);
        AckContent ackContent = new AckContent();
        ackContent.setSrcId(srcId);
        message.setContent(JacksonUtil.object2String(ackContent));
        return message;
    }

    /**
     * 创建ACK消息
     *
     * @param teamId 群组ID
     * @param srcId  源消息ID
     * @return Message
     */
    public static Message createTeamAckMessage(Integer teamId, Long srcId) {
        Message message = new Message();
        message.setTeamId(teamId);
        message.setMsgType(MessageTypeEnum.Ack);
        message.setChatType(ChatTypeEnum.Group);
        AckContent ackContent = new AckContent();
        ackContent.setSrcId(srcId);
        message.setContent(JacksonUtil.object2String(ackContent));
        return message;
    }

    /**
     * 创建ACK消息
     *
     * @param teamId 客服组ID
     * @param srcId  源消息ID
     * @return Message
     */
    public static Message createCustomAckMessage(Integer teamId, Long srcId) {
        Message message = new Message();
        message.setTeamId(teamId);
        message.setMsgType(MessageTypeEnum.Ack);
        message.setChatType(ChatTypeEnum.Custom);
        AckContent ackContent = new AckContent();
        ackContent.setSrcId(srcId);
        message.setContent(JacksonUtil.object2String(ackContent));
        return message;
    }
}
