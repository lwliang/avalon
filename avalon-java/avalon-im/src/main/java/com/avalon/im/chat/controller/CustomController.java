/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.im.chat.controller;

import com.avalon.core.model.RecordRow;
import com.avalon.im.chat.service.CustomChatService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("custom")
public class CustomController {
    private final CustomChatService customChatService;

    public CustomController(CustomChatService customChatService) {
        this.customChatService = customChatService;
    }

    @PostMapping("/get/group")
    public RecordRow getCustomGroup(@RequestBody Map<String, Object> param) {
        int customId = 0;
        if (param.get("customId") != null) {
            customId = Integer.parseInt(param.get("customId").toString());
        }
        return customChatService.getCustomGroup(customId);
    }
}
