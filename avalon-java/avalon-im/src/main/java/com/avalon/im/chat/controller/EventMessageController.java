/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.im.chat.controller;

import com.avalon.im.chat.service.MessageService;
import com.avalon.im.model.Message;
import com.avalon.im.service.MessageEventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("event/message")
public class EventMessageController {
    private final MessageService messageService;
    private final MessageEventService messageEventService;

    public EventMessageController(MessageService messageService,
                                  MessageEventService messageEventService) {
        this.messageService = messageService;
        this.messageEventService = messageEventService;
    }

    @PostMapping("/comment")
    public void sendEventMessage(@RequestBody Message message) {
        messageEventService.handleMessage(null, message);
    }
}
