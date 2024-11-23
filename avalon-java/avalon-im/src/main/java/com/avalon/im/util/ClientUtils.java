/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.im.util;

import com.avalon.im.model.ClientSocket;
import com.avalon.im.model.ClientSocketList;
import com.avalon.im.model.ClientSocketMap;
import io.netty.channel.ChannelHandlerContext;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class ClientUtils {
    public static ClientSocket getClientSocket(ChannelHandlerContext ctx) {
        return ClientSocketMap.getInstance().getClientSocket(ctx);
    }

    public static Boolean isOnline(Integer userId) {
        return ClientSocketMap.getInstance().isOnline(userId);
    }

    public static ClientSocketList getClientSocketByUserId(Integer userId) {
        return ClientSocketMap.getInstance().getClientSocketByUserId(userId);
    }

    public static ConcurrentHashMap<Long, ClientSocket> getAllClientSocket() {
        return ClientSocketMap.getInstance().getAllClientSocket();
    }

    public static void auth(Integer userId, ClientSocket clientSocket) {
        ClientSocketMap.getInstance().auth(userId, clientSocket);
    }

    public static void removeClientSocket(ClientSocket clientSocket) {
        ClientSocketMap.getInstance().removeClientSocket(clientSocket);
    }

    /**
     * 获取所有已认证的客户端
     *
     * @return ClientSocketList
     */
    public static List<Integer> getAllAuthClientSocket() {
        return ClientSocketMap.getInstance().getAllAuthClientSocket();
    }
}
