/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.im.chat.controller;

import com.avalon.core.condition.Condition;
import com.avalon.core.model.RecordRow;
import com.avalon.im.model.Team;
import com.avalon.im.chat.service.TeamChatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@Slf4j
@RequestMapping("team")
public class TeamController {
    @Autowired
    private TeamChatService teamChatService;

    /**
     * 创建群组
     *
     * @param team 群组信息
     * @return 群组信息
     */
    @PostMapping("add")
    public RecordRow addTeam(@RequestBody Team team) {
        return teamChatService.save(team);
    }

    /**
     * 修改群组信息
     *
     * @param team 群组信息
     */
    @PostMapping("update")
    public void modifyTeam(@RequestBody Team team) {
        teamChatService.modify(team);
    }


    /**
     * 删除群组
     *
     * @param param 群组ID
     */
    @PostMapping("delete")
    public void deleteTeam(@RequestBody Map<String, Object> param) {
        String teamId = param.get("teamId").toString();
        teamChatService.delete(Condition.equalCondition("id", teamId));
    }
}
