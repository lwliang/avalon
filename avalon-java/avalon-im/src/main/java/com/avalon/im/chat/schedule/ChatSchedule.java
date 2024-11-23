/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.im.chat.schedule;

import com.avalon.core.context.Context;
import com.avalon.core.model.Record;
import com.avalon.core.model.RecordRow;
import com.avalon.im.chat.service.MessageService;
import com.avalon.im.chat.service.MessageTeamService;
import com.avalon.im.util.ClientUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ChatSchedule {
    private final MessageService messageService;
    private final MessageTeamService messageTeamService;
    private final Context context;

    public ChatSchedule(Context context,
                        MessageService messageService,
                        MessageTeamService messageTeamService) {
        this.context = context;
        this.messageService = messageService;
        this.messageTeamService = messageTeamService;
    }

    @Scheduled(cron = "30 * * * * ?")
    public void checkAckOfflineMessage() {
        context.init(context.getApplicationConfig().getDataSource().getDatabase());
        List<Integer> allAuthClientSocket = ClientUtils.getAllAuthClientSocket();

        // 检测单聊消息
        allAuthClientSocket.forEach(userId -> {
            int count = messageService.getSendMessageUnAckCount(userId);
            if (count <= 0) {
                return;
            }
            Record sendMessageUnAck = messageService.getSendMessageUnAck(userId);
            sendMessageUnAck.forEach(recordRow -> {
                ClientUtils.getClientSocketByUserId(userId).forEach(clientSocket -> {
                    clientSocket.send(recordRow);
                });
            });
        });

        // 检测群聊消息
        allAuthClientSocket.forEach(userId -> {
            int count = messageTeamService.getSendTeamMessageUnAckCount(userId);
            if (count <= 0) {
                return;
            }
            Record sendTeamMessageUnAck = messageTeamService.getSendTeamMessageUnAck(userId);
            sendTeamMessageUnAck.forEach(recordRow -> {
                RecordRow msg = recordRow.getRecordRow("msgId");
                msg.put("toUserId", recordRow.getInteger("toUserId"));
                ClientUtils.getClientSocketByUserId(userId).forEach(clientSocket -> {
                    clientSocket.send(msg);
                });
            });
        });
    }
}
