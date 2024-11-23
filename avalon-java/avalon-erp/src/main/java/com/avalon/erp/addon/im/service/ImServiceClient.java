/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.erp.addon.im.service;

import com.avalon.core.remote.AvalonRemoteClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Map;

@FeignClient(name = "avalon-im")
public interface ImServiceClient extends AvalonRemoteClient {
    @PostMapping("/im/user/register")
    Map<String, Object> registerIm(Map<String, Object> param);

    @PostMapping("/im/event/message/comment")
    void sendCommentEventMessage(Map<String, Object> param);

    @GetMapping("/im/test")
    String test();
}