/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.im.model;


import com.avalon.core.util.ObjectUtils;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Slf4j
public class ClientSocketMap {
    private static final ClientSocketMap INSTANCE = new ClientSocketMap();
    private final ConcurrentHashMap<Long, ClientSocket> clientMap; // 连接id(自动生成) -> 客户端连接
    private final ConcurrentHashMap<Integer, ClientSocketList> userMap; // 用户id -> 客户端连接
    private final ConcurrentHashMap<ChannelHandlerContext, ClientSocket> allClientMap; // 所有连接

    protected ClientSocketMap() {
        clientMap = new ConcurrentHashMap<Long, ClientSocket>();
        userMap = new ConcurrentHashMap<Integer, ClientSocketList>();
        allClientMap = new ConcurrentHashMap<ChannelHandlerContext, ClientSocket>();
    }

    public static ClientSocketMap getInstance() {
        return INSTANCE;
    }

    public ClientSocketList getClientSocketByUserId(Integer userId) {
        return userMap.get(userId);
    }

    /**
     * 获取所有已认证的客户端
     *
     * @return ClientSocketList
     */
    public List<Integer> getAllAuthClientSocket() {
        return new ArrayList<>(userMap.keySet());
    }

    /**
     * 移除客户端
     *
     * @param clientSocket 客户端
     */
    public void removeClientSocket(ClientSocket clientSocket) {
        clientMap.remove(clientSocket.getClientId());
        allClientMap.remove(clientSocket.getChannel());
        if (!clientSocket.isAuth()) { // 未鉴权的客户端
            return;
        }
        ClientSocketList clientSocketList = userMap.get(clientSocket.getUserId());
        if (ObjectUtils.isNotEmpty(clientSocketList)) { // 已鉴权的客户端
            clientSocketList.remove(clientSocket);
            if (clientSocketList.isEmpty()) {
                userMap.remove(clientSocket.getUserId());
            }
        }
        displayClient();
    }

    public Boolean isOnline(Integer userId) {
        return userMap.containsKey(userId);
    }


    /**
     * 添加客户端
     *
     * @param ctx 客户端连接
     * @return 客户端信息
     */
    public ClientSocket getClientSocket(ChannelHandlerContext ctx) {
        if (!allClientMap.containsKey(ctx)) {
            ClientSocket clientSocket = new ClientSocket(ctx);
            allClientMap.put(ctx, clientSocket);
            if (!clientMap.containsKey(clientSocket.getClientId())) {
                clientMap.put(clientSocket.getClientId(), clientSocket);
            }
        }
        displayClient();
        return allClientMap.get(ctx);
    }

    /**
     * 客户端鉴权
     *
     * @param userId       用户id
     * @param clientSocket 客户端
     */
    public void auth(Integer userId, ClientSocket clientSocket) {
        if (userMap.containsKey(userId)) {
            if (!userMap.get(userId).contains(clientSocket)) {
                userMap.get(userId).add(clientSocket);
            }
        } else {
            ClientSocketList clientSocketList = new ClientSocketList();
            clientSocketList.add(clientSocket);
            userMap.put(userId, clientSocketList);
        }
        displayClient();
    }

    public ConcurrentHashMap<Long, ClientSocket> getAllClientSocket() {
        return clientMap;
    }

    private void displayClient() {
        log.debug("allClientMap:{}", allClientMap);
        log.debug("clientMap:{}", clientMap);
        log.debug("userMap:{}", userMap);
    }
}
