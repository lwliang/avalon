/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.im.chat.service;

import com.avalon.core.condition.Condition;
import com.avalon.core.field.Field;
import com.avalon.core.field.Fields;
import com.avalon.core.model.Record;
import com.avalon.core.model.RecordRow;
import com.avalon.core.service.AbstractService;
import com.avalon.core.util.DateTimeUtils;
import com.avalon.im.enums.StateEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MessageTeamService extends AbstractService {
    @Override
    public String getServiceName() {
        return "chat.message.team";
    }

    @Override
    protected Field createPrimaryKeyField() {
        return Fields.createBigIntegerField("id", true, false, true);
    }

    private final Field toUserId = Fields.createInteger("接受人");
    private final Field isRead = Fields.createBoolean("是否已读");
    private final Field serverSendTime = Fields.createTimestamp("消息发送时间戳");
    private final Field stateEnum = Fields.createSelection("消息状态", StateEnum.class);
    private final Field msgId = Fields.createMany2one("消息ID", "chat.message");

    @Override
    public Boolean getNeedDefaultField() {
        return false;
    }

    public void insert(Integer toUerId, Long msgId, StateEnum stateEnum) {
        RecordRow recordRow = RecordRow.build();
        recordRow.put("toUserId", toUerId);
        recordRow.put("stateEnum", stateEnum);
        recordRow.put("msgId", msgId);
        recordRow.put("serverSendTime", DateTimeUtils.getCurrentTimestamp());
        recordRow.put("isRead", false);
        insert(recordRow);
    }

    public void insert(Integer toUerId, Long msgId) {
        insert(toUerId, msgId, StateEnum.ServerToClientIng);
    }

    public void updateAck(Long msgId, Integer toUserId, Integer teamId) {
        RecordRow recordRow = RecordRow.build();
        recordRow.put("stateEnum", StateEnum.Client);
        Condition condition = Condition.andCondition(Condition.equalCondition("msgId", msgId),
                Condition.equalCondition("toUserId", toUserId));
        condition = condition.andCondition(Condition.equalCondition("teamId", teamId));
        update(recordRow, condition);
    }

    public int getSendTeamMessageUnAckCount(Integer userId) {
        Condition condition = getCondition(userId);
        return selectCount(condition);
    }

    public Record getSendTeamMessageUnAck(Integer userId) {
        Condition condition = getCondition(userId);
        return select("id,toUserId,msgId,msgId.fromUserId,msgId.content,msgId.msgType," +
                        "msgId.chatType,msgId.eventType,msgId.teamId",
                condition);
    }

    private Condition getCondition(Integer userId) {
        return Condition.equalCondition(stateEnum, StateEnum.ServerToClientIng)
                .andCondition(Condition.equalCondition(toUserId, userId));
    }
}
