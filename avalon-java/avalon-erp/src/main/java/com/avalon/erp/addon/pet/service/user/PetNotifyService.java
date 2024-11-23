/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.erp.addon.pet.service.user;

import com.avalon.core.condition.Condition;
import com.avalon.core.field.Field;
import com.avalon.core.field.Fields;
import com.avalon.core.model.PrimaryKey;
import com.avalon.core.model.Record;
import com.avalon.core.model.RecordRow;
import com.avalon.core.service.AbstractService;
import com.avalon.core.util.DateTimeUtils;
import com.avalon.core.util.SnowflakeIdWorker4rd;
import com.avalon.erp.addon.im.service.ImService;
import com.avalon.erp.addon.pet.model.enums.NotifyTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PetNotifyService extends AbstractService {
    private final ImService imService;
    private final PetUserService petUserService;

    public PetNotifyService(ImService imService, PetUserService petUserService) {
        this.imService = imService;
        this.petUserService = petUserService;
    }

    @Override
    public String getServiceName() {
        return "pet.notify";
    }

    @Override
    public String getLabel() {
        return "通知";
    }

    public final Field fromUserId = Fields.createMany2one("触发用户id", "pet.user");
    public final Field toUserId = Fields.createMany2one("接收用户id", "pet.user");
    public final Field notify = Fields.createSelection("通知类型", NotifyTypeEnum.class);
    public final Field content = Fields.createString("通知内容");
    public final Field read = Fields.createBoolean("已读", false, false);
    public final Field title = Fields.createString("标题");
    public final Field fromService = Fields.createString("来源服务");
    public final Field fromServiceId = Fields.createInteger("来源服务id");

    public Integer getUnreadCount(Integer userId) {
        return selectCount(toUserId.eq(userId).andCondition(read.eq(false)));
    }

    public Record getLastNotify(Integer userId) {
        return select(1, "id desc", Condition.equalCondition(toUserId, userId),
                "id", "read", "content", "notify", "fromUserId", "toUserId");
    }

    public void sendEventMessage(String title,
                                 String fromService,
                                 Integer fromServiceId,
                                 Integer fromUserId,
                                 Integer toUserId,
                                 NotifyTypeEnum eventType,
                                 RecordRow content) {
        RecordRow row = RecordRow.build();
        row.put(this.title, title);
        row.put(this.fromService, fromService);
        row.put(this.fromServiceId, fromServiceId);
        row.put(this.fromUserId, fromUserId);
        row.put(this.toUserId, toUserId);
        row.put(this.notify, eventType);
        row.put(this.content, content.convert2Json());
        PrimaryKey key = insert(row);
        content.put("notifyId", key.getInteger());

        RecordRow message = RecordRow.build();
        message.put("fromUserId", petUserService.getImUserId(fromUserId));
        message.put("toUserId", petUserService.getImUserId(toUserId));
        message.put("msgType", "Event");
        message.put("eventType", eventType.toString());
        message.put("id", SnowflakeIdWorker4rd.nextUId());
        message.put("timestamp", DateTimeUtils.getCurrentTimestamp());
        message.put("stateEnum", "ClientToServerIng");
        message.put("content", content.convert2Json());

        getContext().getServiceBean(PetNotifyService.class).sendEventMessage(message);
    }

    @Async
    public void sendEventMessage(RecordRow message) {
        imService.sendEventMessage(message);
    }
}
