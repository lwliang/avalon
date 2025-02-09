package com.avalon.erp.sys.addon.im.service;

import com.avalon.core.condition.Condition;
import com.avalon.core.exception.AvalonException;
import com.avalon.core.field.Field;
import com.avalon.core.field.Fields;
import com.avalon.core.model.Record;
import com.avalon.core.model.RecordRow;
import com.avalon.core.service.TransientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class ImChatService extends TransientService {
    @Autowired
    private ImService imService;

    @Override
    public String getServiceName() {
        return "im.chat";
    }

    @Override
    public String getLabel() {
        return "聊天";
    }

    public Field memberIds = Fields.createOne2many("成员",
            "base.user", "id");
    public Field chatIds = Fields.createOne2many("聊天列表",
            "base.user", "id");

    @Override
    public RecordRow create(RecordRow defaultRow) throws AvalonException {
        RecordRow recordRow = super.create(defaultRow);

        Record members = getServiceBean("base.user").select(null,
                "id", "name", "avatar", "imUserId");
        recordRow.put(memberIds, members);

        Record select = getServiceBean("base.user").select(
                Condition.equalCondition("id", getContext().getUserId()),
                "id", "name", "avatar", "imUserId");
        if (!select.isEmpty()) {
            if (select.get(0).isNotNull("imUserId")) {
                Integer imUserId = select.get(0).getInteger("imUserId");
                Record userChatList = imService.getUserChatList(imUserId);
                recordRow.put(chatIds, userChatList);
            }
        } else {
            recordRow.put(chatIds, Record.build());
        }

        return recordRow;
    }
}
