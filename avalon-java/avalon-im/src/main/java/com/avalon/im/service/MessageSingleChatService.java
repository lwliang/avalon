/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.im.service;

import com.avalon.core.exception.AvalonException;
import com.avalon.core.util.ObjectUtils;
import com.avalon.im.chat.service.MessageService;
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
public class MessageSingleChatService implements IMessageService {
    @Autowired
    private MessageService messageService;

    @Override
    public void handleMessage(ClientSocket fromClientSocket, Message message) throws AvalonException {
        ClientSocketList toClientSocketList = ClientUtils.getClientSocketByUserId(message.getToUserId());
        if (log.isDebugEnabled()) {
            log.debug("single chat message: {}", message);
        }
        if (ObjectUtils.isEmpty(toClientSocketList)) { // 用户不在线
            log.info("user {} is offline", message.getToUserId());
            message.setStateEnum(StateEnum.Server);
            messageService.insert(message);
            fromClientSocket.sendAck(fromClientSocket.getUserId(), message.getId());
            return;
        }
        message.setStateEnum(StateEnum.ServerToClientIng);
        messageService.insert(message);
        toClientSocketList.forEach(toClientSocket -> {
            if (!toClientSocket.getChannel().isRemoved()) {
                toClientSocket.send(message);
            }
        });
        fromClientSocket.sendAck(fromClientSocket.getUserId(), message.getId());
    }
}
