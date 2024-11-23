/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.im.service;

import com.avalon.core.context.AvalonApplicationContext;
import com.avalon.core.exception.AvalonException;
import com.avalon.core.util.JacksonUtil;
import com.avalon.im.chat.service.MessageService;
import com.avalon.im.interfaces.IMessageService;
import com.avalon.im.model.AuthContent;
import com.avalon.im.model.ClientSocket;
import com.avalon.im.model.Message;
import com.avalon.im.util.ClientUtils;
import com.avalon.im.util.MessageUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MessageAuthService implements IMessageService {
    @Autowired
    private MessageService messageService;

    @Override
    public void handleMessage(ClientSocket fromClientSocket, Message message) throws AvalonException {
        if (log.isDebugEnabled()) {
            log.debug("auth message: {}", message);
        }
        Integer fromUserId = message.getFromUserId();
        AuthContent authContent;
        try {
            authContent = JacksonUtil.convert2Object(message.getContent(), AuthContent.class);
            fromClientSocket.setUserId(fromUserId);
            fromClientSocket.setToken(authContent.getToken());
            ClientUtils.auth(fromUserId, fromClientSocket);
            int offlineMessageCount = messageService.getOfflineMessageCount(fromClientSocket.getUserId());
            if (offlineMessageCount > 0) {
                Message offlineMessage = MessageUtils.createOfflineMessage(fromUserId, offlineMessageCount);
                fromClientSocket.send(offlineMessage);
            }
        } catch (Exception e) {
            log.error(String.format("auth message error: {}", e.getMessage()), e);
            // throw new AvalonException("auth message error");
        }
    }
}
