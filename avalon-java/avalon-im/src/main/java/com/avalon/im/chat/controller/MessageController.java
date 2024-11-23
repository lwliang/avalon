/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.im.chat.controller;

import com.avalon.core.model.Record;
import com.avalon.core.model.RecordRow;
import com.avalon.im.chat.service.MessageService;
import com.avalon.im.util.MessageUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@Slf4j
@RequestMapping("message")
public class MessageController {
    @Autowired
    private MessageService messageService;

    @PostMapping("offline")
    public Record getOfflineMessage(@RequestBody Map<String, Object> params) {
        int userId = 0;
        if (params.containsKey("userId")) {
            userId = Integer.parseInt(params.get("userId").toString());
        }
        return messageService.getOfflineMessage(userId);
    }

    @PostMapping("get/id")
    public RecordRow getMessageId(@RequestBody Map<String, Object> params) {
        RecordRow row = RecordRow.build();
        row.put("id", MessageUtils.getMsgId());
        return row;
    }

    @PostMapping("/user/get/page")
    public Record getMessagePage(@RequestBody Map<String, Object> params) {
        int toUserId = 0;
        if (params.containsKey("toUserId")) {
            toUserId = Integer.parseInt(params.get("toUserId").toString());
        }
        int fromUserId = 0;
        if (params.containsKey("fromUserId")) {
            fromUserId = Integer.parseInt(params.get("fromUserId").toString());
        }
        int pageSize = 10;
        if (params.containsKey("pageSize")) {
            pageSize = Integer.parseInt(params.get("pageSize").toString());
        }
        int pageNum = 10;
        if (params.containsKey("pageNum")) {
            pageNum = Integer.parseInt(params.get("pageNum").toString());
        }
        return messageService.getMessagePage(fromUserId, toUserId, pageNum, pageSize);
    }
}
