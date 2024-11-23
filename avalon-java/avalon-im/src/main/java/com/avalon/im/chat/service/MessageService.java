/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.im.chat.service;

import com.avalon.core.condition.Condition;
import com.avalon.core.field.Field;
import com.avalon.core.field.Fields;
import com.avalon.core.model.*;
import com.avalon.core.model.Record;
import com.avalon.core.service.AbstractService;
import com.avalon.core.util.JacksonUtil;
import com.avalon.im.enums.ChatTypeEnum;
import com.avalon.im.enums.EventTypeEnum;
import com.avalon.im.enums.MessageTypeEnum;
import com.avalon.im.enums.StateEnum;
import com.avalon.im.model.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MessageService extends AbstractService {
    private final ChatListService chatListService;

    public MessageService(ChatListService chatListService) {
        this.chatListService = chatListService;
    }

    @Override
    public String getServiceName() {
        return "chat.message";
    }

    @Override
    public String getLabel() {
        return "消息记录";
    }

    private final Field fromUserId = Fields.createInteger("发送人");
    private final Field toUserId = Fields.createInteger("接收人");
    private final Field content = Fields.createText("消息内容");
    private final Field msgType = Fields.createSelection("消息类型", MessageTypeEnum.class);
    private final Field chatType = Fields.createSelection("聊天类型", ChatTypeEnum.class);
    private final Field eventType = Fields.createSelection("事件类型", EventTypeEnum.class);
    private final Field stateEnum = Fields.createSelection("消息状态", StateEnum.class);
    private final Field isRead = Fields.createBoolean("是否已读");
    private final Field timestamp = Fields.createTimestamp("消息发送时间戳");
    private final Field serverSendTime = Fields.createTimestamp("消息服务器发送时间");
    private final Field teamId = Fields.createInteger("群组ID");
    private final Field memberMsg = Fields.createOne2many("群成员消息", "chat.message.team",
            "msgId");

    @Override
    protected Field createPrimaryKeyField() {
        return Fields.createBigIntegerField("消息ID", true, false, true);
    }

    @Override
    public Boolean getNeedDefaultField() {
        return false;
    }

    public PrimaryKey insert(Message message) {
        RecordRow recordRow = JacksonUtil.convert2Map(message);
        if (recordRow.containsKey(timestamp)) {
            recordRow.put(timestamp, message.getTimestamp());
        }
        PrimaryKey key = insert(recordRow);
        chatListService.insert(message.getFromUserId(),
                message.getToUserId(),
                message.getChatType(),
                key.getLong(),
                0); // 创建聊天列表 创建自己的聊天列表
        chatListService.insert(message.getToUserId(),
                message.getFromUserId(),
                message.getChatType(),
                key.getLong(),
                1); // 创建聊天列表 创建对方的聊天列表
        return key;
    }

    /**
     * 获取离线消息数量
     *
     * @param userId 用户ID
     * @return 离线消息数量
     */
    public int getOfflineMessageCount(Integer userId) {
        Condition condition = Condition.equalCondition(stateEnum, StateEnum.Server)
                .andCondition(Condition.equalCondition(toUserId, userId));
        return selectCount(condition);
    }

    /**
     * 获取未确认的消息
     *
     * @param userId 用户ID
     * @return 未确认的消息
     */
    public Record getSendMessageUnAck(Integer userId) {
        Condition condition = Condition.equalCondition(toUserId, userId);
        condition = condition.andCondition(stateEnum, StateEnum.ServerToClientIng);

        return select(condition);
    }

    /**
     * 获取未确认的消息数量
     *
     * @param userId 用户ID
     * @return 未确认的消息数量
     */
    public int getSendMessageUnAckCount(Integer userId) {
        Condition condition = getAckCondition(userId);
        return selectCount(condition);
    }

    /**
     * 获取离线消息
     *
     * @param userId 用户ID
     * @return 离线消息
     */
    public Record getOfflineMessage(Integer userId) {
        Condition condition = Condition.equalCondition(toUserId, userId);
        condition = condition.andInCondition(stateEnum, StateEnum.Server, StateEnum.ServerToClientIng);
        return select(condition);
    }

    /**
     * 获取单聊
     *
     * @param toUserId
     * @param fromUserId
     * @param pageNum
     * @param pageSize
     * @return
     */
    public Record getMessagePage(Integer fromUserId, Integer toUserId, Integer pageNum, Integer pageSize) {
        PageParam pageParam = new PageParam(pageNum, pageSize);
        Condition condition = Condition.equalCondition(this.toUserId, toUserId);
        condition = condition.andEqualCondition(this.fromUserId, fromUserId);

        Condition condition1 = Condition.equalCondition(this.fromUserId, toUserId);
        condition1 = condition1.andEqualCondition(this.toUserId, fromUserId);
        condition = condition.orCondition(condition1);
        PageInfo pageInfo = selectPage(pageParam, "id desc", condition,
                getAllFieldName().toArray(new String[0]));
        return pageInfo.getData();
    }

    private Condition getAckCondition(Integer userId) {
        Condition condition = Condition.equalCondition(toUserId, userId);
        condition = condition.andCondition(stateEnum, StateEnum.ServerToClientIng);
//        condition = condition.andCondition(Condition.subGreaterThanSpanCondition(serverSendTime,
//                System.currentTimeMillis(),
//                30 * 1000));
        return condition;
    }

    /**
     * 更新ack消息
     *
     * @param id 消息ID
     */
    public void updateAck(Long id) {
        RecordRow row = new RecordRow();
        row.put(this.stateEnum, StateEnum.Client);
        row.put(this.getPrimaryKeyField(), id);
        update(row);
    }
}
