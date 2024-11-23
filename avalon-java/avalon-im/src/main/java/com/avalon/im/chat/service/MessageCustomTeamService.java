/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.im.chat.service;

import com.avalon.core.field.Field;
import com.avalon.core.field.Fields;
import com.avalon.core.model.RecordRow;
import com.avalon.core.service.AbstractService;
import com.avalon.core.util.DateTimeUtils;
import com.avalon.im.enums.StateEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MessageCustomTeamService extends AbstractService {
    @Override
    public String getServiceName() {
        return "chat.message.custom.team";
    }
    @Override
    public Boolean getNeedDefaultField() {
        return false;
    }

    @Override
    protected Field createPrimaryKeyField() {
        return Fields.createBigIntegerField("id", true, true, true);
    }

    private final Field toUserId = Fields.createInteger("接受人");
    private final Field isRead = Fields.createBoolean("是否已读");
    private final Field serverSendTime = Fields.createTimestamp("消息发送时间戳");
    private final Field stateEnum = Fields.createSelection("消息状态", StateEnum.class);
    private final Field msgId = Fields.createBigIntegerField("消息ID");

    public void insert(Integer toUerId, Long msgId, StateEnum stateEnum) {
        RecordRow recordRow = RecordRow.build();
        recordRow.put("toUserId", toUerId);
        recordRow.put("stateEnum", stateEnum);
        recordRow.put("msgId", msgId);
        recordRow.put("serverSendTime", DateTimeUtils.getCurrentTimestamp());
        recordRow.put("isRead", false);
        insert(recordRow);
    }
}
