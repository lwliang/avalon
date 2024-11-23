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
public class TeamChatMemberService extends AbstractService {
    @Override
    public String getServiceName() {
        return "chat.team.member";
    }

    private final Field teamId = Fields.createMany2one("群组ID", "chat.team");
    private final Field userId = Fields.createInteger("用户ID");
    @Override
    public Boolean getNeedDefaultField() {
        return false;
    }
}
