/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.erp.addon.im.service;

import com.avalon.core.model.RecordRow;
import com.avalon.core.util.DateTimeUtils;
import com.avalon.core.util.SnowflakeIdWorker4rd;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class ImService {
    private final ImServiceClient imServiceClient;

    public ImService(ImServiceClient imServiceClient) {
        this.imServiceClient = imServiceClient;
    }

    public Integer registerIm(String company, String app, Integer thirdUserId) {
        log.info(imServiceClient.test());
        Map<String, Object> param = new HashMap<>();
        param.put("company", company);
        param.put("app", app);
        param.put("thirdUserId", thirdUserId);
        Map<String, Object> stringObjectMap = imServiceClient.registerIm(param);
        return Integer.parseInt(stringObjectMap.get("userId").toString());
    }

    /**
     * 发送事件通知
     *
     * @param message
     */
    public void sendEventMessage(RecordRow message) {
        imServiceClient.sendCommentEventMessage(message.convert2Map());
    }


    public void sendEventMessage(Integer fromUserId, Integer toUserId, String eventType, String content) {
        RecordRow message = RecordRow.build();
        message.put("fromUserId", fromUserId);
        message.put("toUserId", toUserId);
        message.put("msgType", "Event");
        message.put("eventType", eventType);
        message.put("id", SnowflakeIdWorker4rd.nextUId());
        message.put("timestamp", DateTimeUtils.getCurrentTimestamp());
        message.put("stateEnum", "ClientToServerIng");
        message.put("content", content);

        sendEventMessage(message);
    }
}
