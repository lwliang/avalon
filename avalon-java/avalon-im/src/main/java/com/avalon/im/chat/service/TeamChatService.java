/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.im.chat.service;

import com.avalon.core.field.Field;
import com.avalon.core.field.Fields;
import com.avalon.core.model.RecordRow;
import com.avalon.core.service.AbstractService;
import com.avalon.core.util.JacksonUtil;
import com.avalon.core.util.RecordRowUtils;
import com.avalon.im.model.Team;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
public class TeamChatService extends AbstractService {
    @Override
    public String getServiceName() {
        return "chat.team";
    }
    @Override
    public Boolean getNeedDefaultField() {
        return false;
    }

    private final Field members = Fields.createOne2many("群成员", "chat.team.member", "teamId");

    /**
     * 创建群组
     *
     * @param team 群组信息
     * @return
     */
    public RecordRow save(Team team) {
        Map map = JacksonUtil.bean2Map(team);
        RecordRow recordRow = RecordRowUtils.convert(this.getService(), map);
        insert(recordRow);
        return recordRow;
    }

    /**
     * 修改群组信息
     *
     * @param team 群组信息
     */
    public void modify(Team team) {
        Map map = JacksonUtil.bean2Map(team);
        RecordRow recordRow = RecordRowUtils.convert(this.getService(), map);
        update(recordRow);
    }
}
