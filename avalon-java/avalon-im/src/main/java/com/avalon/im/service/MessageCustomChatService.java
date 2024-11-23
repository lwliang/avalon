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
import com.avalon.im.chat.service.*;
import com.avalon.im.enums.StateEnum;
import com.avalon.im.interfaces.IMessageService;
import com.avalon.im.model.ClientSocket;
import com.avalon.im.model.ClientSocketList;
import com.avalon.im.model.Message;
import com.avalon.im.util.ClientUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MessageCustomChatService implements IMessageService {
    private final CustomChatMemberService customChatMemberService;
    private final MessageCustomService messageCustomService;
    private final MessageCustomTeamService messageCustomTeamService;

    public MessageCustomChatService(CustomChatMemberService customChatMemberService,
                                    MessageCustomService messageCustomService,
                                    MessageCustomTeamService messageTeamService) {
        this.customChatMemberService = customChatMemberService;
        this.messageCustomService = messageCustomService;
        this.messageCustomTeamService = messageTeamService;
    }

    @Override
    public void handleMessage(ClientSocket fromClientSocket, Message message) throws AvalonException {
        Integer teamId = message.getTeamId();
        Record teamMembers = customChatMemberService.select("customUserId", Condition.equalCondition("customTeamId", teamId));
        if (log.isDebugEnabled()) {
            log.debug("custom chat message: {}", message);
        }
        PrimaryKey msgId = messageCustomService.insert(message);
        fromClientSocket.sendTeamAck(message.getTeamId(), msgId.getLong());
        for (RecordRow teamMember : teamMembers) {
            Integer userId = teamMember.getInteger("customUserId");
            if (userId.equals(fromClientSocket.getUserId())) { // 不给自己发
                messageCustomTeamService.insert(userId, msgId.getLong(), StateEnum.Client);
                continue;
            }
            ClientSocketList clientSocketByUserId = ClientUtils.getClientSocketByUserId(userId);
            if (ObjectUtils.isEmpty(clientSocketByUserId)) {
                messageCustomTeamService.insert(userId, msgId.getLong(), StateEnum.Server);
                continue;
            }
            clientSocketByUserId.forEach(clientSocket -> { // 给群组成员发消息
                if (!clientSocket.getChannel().isRemoved()) {
                    messageCustomTeamService.insert(userId, msgId.getLong(), StateEnum.ServerToClientIng);
                    clientSocket.send(message);
                }
            });
        }
    }
}
