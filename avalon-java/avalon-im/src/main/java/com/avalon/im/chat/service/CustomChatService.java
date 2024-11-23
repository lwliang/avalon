/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.im.chat.service;

import com.avalon.core.field.Field;
import com.avalon.core.field.Fields;
import com.avalon.core.model.Record;
import com.avalon.core.model.RecordRow;
import com.avalon.core.service.AbstractService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CustomChatService extends AbstractService {
    @Override
    public String getServiceName() {
        return "chat.custom";
    }

    @Override
    public String getLabel() {
        return "客服群组";
    }

    @Override
    public Boolean getNeedDefaultField() {
        return false;
    }

    public final Field customUserId = Fields.createInteger("客服ID"); // 主客服ID
    public final Field chatUserId = Fields.createInteger("聊天用户ID"); // 聊天用户ID

    public RecordRow getCustomGroup(Integer customUserId) {
        Record select = select(chatUserId.eq(customUserId), "id");
        RecordRow recordRow;
        if (select.isEmpty()) {
            recordRow = RecordRow.build();
            recordRow.put(chatUserId, customUserId);
            insert(recordRow);
        } else{
            recordRow = select.get(0);
        }
        return recordRow;
    }
}
