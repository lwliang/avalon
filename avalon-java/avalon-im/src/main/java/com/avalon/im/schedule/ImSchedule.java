/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.im.schedule;

import com.avalon.core.context.Context;
import com.avalon.core.util.DateTimeUtils;
import com.avalon.im.model.ClientSocketList;
import com.avalon.im.util.ClientUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

@Component
@Slf4j
public class ImSchedule {
    @Autowired
    private Context context;

    @Scheduled(cron = "5 * * * * ?")
    public void checkHeartBeat() {
        if (context.getApplicationConfig().getDebug()) {
            return;
        }
        ClientSocketList delList = new ClientSocketList();
        Timestamp cur = DateTimeUtils.getCurrentTimestamp();
        ClientUtils.getAllClientSocket().forEach((clientId, clientSocket) -> {
            if (!(clientSocket.isActive() && clientSocket.isOpen())) { // 通道非活跃或已关闭
                delList.add(clientSocket);
                return;
            }
            if (cur.getTime() - clientSocket.getLastHeartbeatTime().getTime() > 1000 * 30) { // 超时30s
                if (!(clientSocket.isActive() && clientSocket.isOpen())) { // 通道非活跃或已关闭
                    delList.add(clientSocket);
                }
            }
        });
        if (!delList.isEmpty()) {
            log.info("因超过30秒，未进行心跳通讯，删除连接，{}", delList);
        }
        delList.forEach((clientSocket -> {
            clientSocket.close();
            ClientUtils.removeClientSocket(clientSocket);
        }));
    }
}
