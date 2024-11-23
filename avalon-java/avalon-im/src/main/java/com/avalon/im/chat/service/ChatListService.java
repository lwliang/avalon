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
import com.avalon.core.util.ObjectUtils;
import com.avalon.im.enums.ChatTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ChatListService extends AbstractService {
    @Override
    public String getServiceName() {
        return "chat.chat.list";
    }

    public final Field fromUserId = Fields.createInteger("发送人");
    public final Field chatType = Fields.createSelection("聊天类型", ChatTypeEnum.class);
    public final Field toUserId = Fields.createInteger("聊天id"); // 单聊是用户id，群聊则是群id
    public final Field teamId = Fields.createInteger("团队");
    public final Field top = Fields.createBoolean("置顶");
    public final Field unReadCount = Fields.createInteger("未读数量", 0);

    public final Field lastMsgId = Fields.createMany2one("最后一条消息",
            "chat.message");
    public final Field lastMsgDate = Fields.createDateTime("最后一条信息日期");

    public void clearUnReadCount(int id) {
        RecordRow row = RecordRow.build();
        row.put("id", id);
        row.put(unReadCount, 0);
        update(row);
    }

    public void clearUnReadCount(int fromUserId, int toUserId) {
        Record select = select(Condition.equalCondition("fromUserId", fromUserId)
                        .andEqualCondition("toUserId", toUserId),
                "id");
        RecordRow recordRow = null;
        if (!select.isEmpty()) {
            recordRow = select.get(0);
            RecordRow row = RecordRow.build();
            row.put("id", recordRow.getInteger("id"));
            row.put(unReadCount, 0);
            update(row);
        }
    }

    public void insert(Integer fromUserId,
                       Integer toUserId,
                       ChatTypeEnum chatType,
                       Long msgId,
                       Integer unReadCount) {
        if (chatType == ChatTypeEnum.Single) {
            Condition condition = Condition.equalCondition("fromUserId", fromUserId);
            condition = condition.andEqualCondition("toUserId", toUserId);
            condition = condition.andEqualCondition("chatType", chatType);
            Record select = select(condition, "id", "unReadCount");
            RecordRow recordRow = null;
            if (!select.isEmpty()) {
                recordRow = select.get(0);
                recordRow.put("unReadCount", recordRow.getInteger("unReadCount") + unReadCount);
                recordRow.put(lastMsgId, msgId);
                recordRow.put(lastMsgDate, DateTimeUtils.getCurrentDate());
                update(recordRow);
            } else {
                recordRow = RecordRow.build();
                recordRow.put("fromUserId", fromUserId);
                recordRow.put("toUserId", toUserId);
                recordRow.put("chatType", chatType);
                recordRow.put(lastMsgId, msgId);
                recordRow.put(lastMsgDate, DateTimeUtils.getCurrentDate());
                recordRow.put("unReadCount", unReadCount);
                recordRow.put("top", false);
                insert(recordRow);
            }
        }
    }
}
