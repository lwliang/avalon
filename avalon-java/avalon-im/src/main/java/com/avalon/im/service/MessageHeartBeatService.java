/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.im.service;

import com.avalon.core.exception.AvalonException;
import com.avalon.core.util.DateTimeUtils;
import com.avalon.im.interfaces.IMessageService;
import com.avalon.im.model.ClientSocket;
import com.avalon.im.model.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MessageHeartBeatService implements IMessageService {
    @Override
    public void handleMessage(ClientSocket fromClientSocket, Message message) throws AvalonException {
        if (log.isDebugEnabled()) {
            log.debug("heart beat message: {}", message);
        }
        fromClientSocket.setLastHeartbeatTime(DateTimeUtils.getCurrentTimestamp());
    }
}
