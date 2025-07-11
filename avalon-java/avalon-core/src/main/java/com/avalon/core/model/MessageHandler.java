package com.avalon.core.model;

import javax.mail.Message;

/**
 * @author lwlianghehe@gmail.com
 * @date 2025/06/23 20:22
 */
public interface MessageHandler {
    void handle(Message message) throws Exception;
}