/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.im.service;

import com.avalon.core.condition.Condition;
import com.avalon.core.exception.AvalonException;
import com.avalon.core.model.PrimaryKey;
import com.avalon.core.model.Record;
import com.avalon.core.model.RecordRow;
import com.avalon.core.util.ObjectUtils;
import com.avalon.im.chat.service.MessageService;
import com.avalon.im.chat.service.MessageTeamService;
import com.avalon.im.chat.service.TeamChatMemberService;
import com.avalon.im.enums.StateEnum;
import com.avalon.im.interfaces.IMessageService;
import com.avalon.im.model.ClientSocket;
import com.avalon.im.model.ClientSocketList;
import com.avalon.im.model.Message;
import com.avalon.im.util.ClientUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MessageTeamChatService implements IMessageService {
    @Autowired
    private TeamChatMemberService teamChatMemberService;
    @Autowired
    private MessageService messageService;
    @Autowired
    private MessageTeamService messageTeamService;

    @Override
    public void handleMessage(ClientSocket fromClientSocket, Message message) throws AvalonException {
        Integer teamId = message.getTeamId();
        Record teamMembers = teamChatMemberService.select("userId", Condition.equalCondition("teamId", teamId));
        if (log.isDebugEnabled()) {
            log.debug("team chat message: {}", message);
        }
        PrimaryKey msgId = messageService.insert(message);
        fromClientSocket.sendTeamAck(message.getTeamId(), msgId.getLong());
        for (RecordRow teamMember : teamMembers) {
            Integer userId = teamMember.getInteger("userId");
            if (userId.equals(fromClientSocket.getUserId())) { // 不给自己发
                messageTeamService.insert(userId, msgId.getLong(), StateEnum.Client);
                continue;
            }
            ClientSocketList clientSocketByUserId = ClientUtils.getClientSocketByUserId(userId);
            if (ObjectUtils.isEmpty(clientSocketByUserId)) {
                messageTeamService.insert(userId, msgId.getLong(), StateEnum.Server);
                continue;
            }
            messageTeamService.insert(userId, msgId.getLong(), StateEnum.ServerToClientIng);
            clientSocketByUserId.forEach(clientSocket -> { // 给群组成员发消息
                if (!clientSocket.getChannel().isRemoved()) {
                    clientSocket.send(message);
                }
            });
        }
    }
}
