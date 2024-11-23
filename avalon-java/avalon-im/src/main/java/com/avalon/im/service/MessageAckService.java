/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.im.service;

import com.avalon.core.exception.AvalonException;
import com.avalon.core.util.JacksonUtil;
import com.avalon.im.chat.service.MessageService;
import com.avalon.im.chat.service.MessageTeamService;
import com.avalon.im.enums.ChatTypeEnum;
import com.avalon.im.interfaces.IMessageService;
import com.avalon.im.model.AckContent;
import com.avalon.im.model.ClientSocket;
import com.avalon.im.model.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MessageAckService implements IMessageService {
    @Autowired
    private MessageService messageService;
    @Autowired
    private MessageTeamService messageTeamService;

    @Override
    public void handleMessage(ClientSocket fromClientSocket, Message message) throws AvalonException {
        if (log.isInfoEnabled()) {
            log.info("ack message: {}", message);
        }
        String content = message.getContent();
        AckContent ackContent = JacksonUtil.convert2Object(content, AckContent.class);
        if (message.getChatType() == ChatTypeEnum.Single) {
            messageService.updateAck(ackContent.getSrcId());
        } else {
            messageTeamService.updateAck(ackContent.getSrcId(), message.getFromUserId(), message.getTeamId());
        }
    }
}
