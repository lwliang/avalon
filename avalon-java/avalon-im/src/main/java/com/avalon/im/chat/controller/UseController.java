/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.im.chat.controller;

import com.avalon.core.condition.Condition;
import com.avalon.core.model.Record;
import com.avalon.core.model.RecordRow;
import com.avalon.core.util.FieldUtils;
import com.avalon.core.util.FieldValue;
import com.avalon.im.chat.service.ChatListService;
import com.avalon.im.chat.service.ChatUserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/user")
public class UseController {
    private final ChatUserService chatUserService;
    private final ChatListService chatListService;

    public UseController(ChatUserService chatUserService,
                         ChatListService chatListService) {
        this.chatUserService = chatUserService;
        this.chatListService = chatListService;
    }

    @PostMapping("/register")
    public RecordRow register(@RequestBody Map<String, Object> param) {
        String company = "";
        if (param.containsKey("company")) {
            company = param.get("company").toString();
        }
        String app = "";
        if (param.containsKey("app")) {
            app = param.get("app").toString();
        }
        String thirdUserId = "";
        if (param.containsKey("thirdUserId")) {
            thirdUserId = param.get("thirdUserId").toString();
        }
        RecordRow row = RecordRow.build();
        row.put("userId", chatUserService.register(company, app, thirdUserId));
        return row;
    }

    @PostMapping("chat/clear/unread")
    public void clearUnRead(@RequestBody Map<String, Object> param) {
        int id = 0;
        if (param.containsKey("id")) {
            id = Integer.parseInt(param.get("id").toString());
        }
        chatListService.clearUnReadCount(id);
    }

    @PostMapping("chat/clear/unread2")
    public void clearUnReadByUserId(@RequestBody Map<String, Object> param) {
        int fromUserId = 0;
        if (param.containsKey("fromUserId")) {
            fromUserId = Integer.parseInt(param.get("fromUserId").toString());
        }
        int toUserId = 0;
        if (param.containsKey("toUserId")) {
            toUserId = Integer.parseInt(param.get("toUserId").toString());
        }
        chatListService.clearUnReadCount(fromUserId, toUserId);
    }

    @PostMapping("/delete/chat/list")
    public void deleteUserChatList(@RequestBody Map<String, Object> param) {
        int id = 0;
        if (param.containsKey("id")) {
            id = Integer.parseInt(param.get("id").toString());
        }
        chatListService.delete(id);
    }

    @PostMapping("chat/list")
    public Record getUserChatList(@RequestBody Map<String, Object> param) {
        int fromUserId = 0;
        if (param.containsKey("userId")) {
            fromUserId = Integer.parseInt(param.get("userId").toString());
        }
        String fields = "id,fromUserId,chatType,toUserId,teamId,top,unReadCount,updateTime,createTime,lastMsgDate" +
                ",lastMsgId.id,lastMsgId.msgType,lastMsgId.content,lastMsgId.timestamp";
        Condition condition = Condition.equalCondition("fromUserId", fromUserId);
        String order = "top asc, id desc, updateTime desc, createTime desc";
        return chatListService.select(order, condition, FieldUtils.getFieldArray(fields));
    }

    @PostMapping("chat/unread/count")
    public RecordRow getUserChatUnreadCount(@RequestBody Map<String, Object> param) {
        int fromUserId = 0;
        if (param.containsKey("userId")) {
            fromUserId = Integer.parseInt(param.get("userId").toString());
        }
        RecordRow row = RecordRow.build();
        FieldValue count = chatListService.getSumFieldValue("unReadCount",
                Condition.equalCondition("fromUserId", fromUserId));
        row.put("count", count.getInteger());
        return row;
    }
}
