/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.im.chat.controller;

import com.avalon.core.condition.Condition;
import com.avalon.core.model.Record;
import com.avalon.core.model.RecordRow;
import com.avalon.im.chat.service.ChatListService;
import com.avalon.im.chat.service.ChatUserService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/")
public class HomeController {
    @GetMapping("test")
    public String test(){
        return "Hello im";
    }
}
