/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.im.interfaces;

import com.avalon.core.exception.AvalonException;
import com.avalon.im.model.ClientSocket;
import com.avalon.im.model.Message;

public interface IMessageService {
    void handleMessage(ClientSocket fromClientSocket, Message message) throws AvalonException;
}
