/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.im.chat.service;

import com.avalon.core.field.Field;
import com.avalon.core.field.Fields;
import com.avalon.core.service.AbstractService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CustomChatMemberService extends AbstractService {
    @Override
    public String getServiceName() {
        return "chat.custom.member";
    }

    @Override
    public String getLabel() {
        return "客服群组成员";
    }

    @Override
    public Boolean getNeedDefaultField() {
        return false;
    }

    public final Field customUserId = Fields.createInteger("客服ID"); // 客服ID
    public final Field customTeamId = Fields.createInteger("客服群组ID"); // 客服群组ID
}
