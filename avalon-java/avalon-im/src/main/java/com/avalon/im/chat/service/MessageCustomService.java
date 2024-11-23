/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.im.chat.service;

import com.avalon.core.field.Field;
import com.avalon.core.field.Fields;
import com.avalon.core.model.PrimaryKey;
import com.avalon.core.service.AbstractService;
import com.avalon.core.util.JacksonUtil;
import com.avalon.im.enums.ChatTypeEnum;
import com.avalon.im.enums.EventTypeEnum;
import com.avalon.im.enums.MessageTypeEnum;
import com.avalon.im.enums.StateEnum;
import com.avalon.im.model.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MessageCustomService extends AbstractService {
    @Override
    public String getServiceName() {
        return "chat.message.custom";
    }

    @Override
    public String getLabel() {
        return "客服消息记录";
    }
    @Override
    public Boolean getNeedDefaultField() {
        return false;
    }

    @Override
    protected Field createPrimaryKeyField() {
        return Fields.createBigIntegerField("id", true, true, true);
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
    private final Field teamId = Fields.createInteger("客服群组ID");

    public PrimaryKey insert(Message message) {
        return insert(JacksonUtil.convert2Map(message));
    }
}
