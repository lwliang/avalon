/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.im.model;

import com.avalon.core.model.RecordRow;
import com.avalon.core.util.DateTimeUtils;
import com.avalon.core.util.JacksonUtil;
import com.avalon.core.util.ObjectUtils;
import com.avalon.core.util.SnowflakeIdWorker3rd;
import com.avalon.im.util.MessageUtils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.sql.Timestamp;

@Slf4j
public class ClientSocket {
    private final String host;
    private final int port;
    private final ChannelHandlerContext channel;
    private String token;
    private Integer userId;

    private final long clientId;

    private Timestamp lastHeartbeatTime = DateTimeUtils.getCurrentTimestamp();

    public void setLastHeartbeatTime(Timestamp lastHeartbeatTime) {
        this.lastHeartbeatTime = lastHeartbeatTime;
    }

    public Timestamp getLastHeartbeatTime() {
        return lastHeartbeatTime;
    }

    public long getClientId() {
        return clientId;
    }

    public boolean isAuth() {
        return ObjectUtils.isNotEmpty(token);
    }

    public ClientSocket(ChannelHandlerContext channel) {
        InetSocketAddress client = (InetSocketAddress) channel.channel().remoteAddress();
        host = client.getAddress().getHostAddress();
        port = client.getPort();
        clientId = SnowflakeIdWorker3rd.nextUId();
        this.channel = channel;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public ChannelHandlerContext getChannel() {
        return channel;
    }

    public boolean isOpen() {
        return channel.channel().isOpen();
    }

    public void close() {
        channel.channel().close();
    }

    public boolean isActive() {
        return channel.channel().isActive();
    }

    public void send(Message msg) {
        if (ObjectUtils.isNotNull(channel) && !channel.isRemoved()) {
            channel.writeAndFlush(new TextWebSocketFrame(JacksonUtil.object2String(msg)));
        }
    }

    /**
     * 发送ACK消息
     *
     * @param toUserId 接收人
     * @param srcId    源消息ID
     */
    public void sendAck(Integer toUserId, Long srcId) {
        Message ackMessage = MessageUtils.createAckMessage(toUserId, srcId);
        if (log.isDebugEnabled()) {
            log.debug("send ack message:{}", ackMessage);
        }
        send(ackMessage);
    }

    /**
     * 发送team ACK消息
     *
     * @param teamId 群组ID
     * @param srcId  源消息ID
     */
    public void sendTeamAck(Integer teamId, Long srcId) {
        Message ackMessage = MessageUtils.createTeamAckMessage(teamId, srcId);
        if (log.isDebugEnabled()) {
            log.debug("send team ack message:{}", ackMessage);
        }
        send(ackMessage);
    }

    /**
     * 发送客服 ACK消息
     *
     * @param teamId 客服组ID
     * @param srcId  源消息ID
     */
    public void sendCustomAck(Integer teamId, Long srcId) {
        Message ackMessage = MessageUtils.createCustomAckMessage(teamId, srcId);
        if (log.isDebugEnabled()) {
            log.debug("send custom ack message:{}", ackMessage);
        }
        send(ackMessage);
    }

    public void send(RecordRow msg) {
        if (isOpen()) {
            channel.writeAndFlush(new TextWebSocketFrame(msg.convert2Json()));
        } else {
            log.error("{} 已关闭，消息{}发送失败", this, msg);
        }
    }

    public boolean isRemoved() {
        return channel.isRemoved();
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "host:" + host + ",port:" + port + ",clientId:" + clientId;
    }
}
