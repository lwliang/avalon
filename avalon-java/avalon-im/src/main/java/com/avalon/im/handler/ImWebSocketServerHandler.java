/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.im.handler;

import com.avalon.core.context.AvalonApplicationContext;
import com.avalon.core.exception.AvalonException;
import com.avalon.core.util.JacksonUtil;
import com.avalon.im.chat.service.MessageService;
import com.avalon.im.enums.ChatTypeEnum;
import com.avalon.im.enums.MessageTypeEnum;
import com.avalon.im.model.ClientSocket;
import com.avalon.im.model.ErrorContent;
import com.avalon.im.model.Message;
import com.avalon.im.service.*;
import com.avalon.im.util.ClientUtils;
import com.avalon.im.util.MessageUtils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import lombok.extern.slf4j.Slf4j;


/**
 * 自定义的Handler需要继承Netty规定好的HandlerAdapter
 * 才能被Netty框架所关联，有点类似SpringMVC的适配器模式
 **/
@Slf4j
public class ImWebSocketServerHandler extends SimpleChannelInboundHandler<WebSocketFrame> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, WebSocketFrame msg) throws Exception {
        if (msg instanceof TextWebSocketFrame) { // 此处仅处理 Text Frame
            String originMsgStr = ((TextWebSocketFrame) msg).text();
            originMsgStr = originMsgStr.replace("“", "\"");
            originMsgStr = originMsgStr.replace("”", "\"");
            if (log.isDebugEnabled()) {
                log.debug("receive data: {}", originMsgStr);
            }
            Message message;
            try {
                message = JacksonUtil.convert2Object(originMsgStr, Message.class);
            } catch (Exception e) {
                log.error(String.format("json convert error: %s", e.getMessage()), e);
                throw new AvalonException("message must be json");
            }
            if (message.getMsgType() == MessageTypeEnum.Auth) {
                AvalonApplicationContext.getInstance().getBean(MessageAuthService.class).handleMessage(ClientUtils.getClientSocket(ctx), message);
                return;
            } else if (message.getMsgType() == MessageTypeEnum.Ack) {
                AvalonApplicationContext.getInstance().getBean(MessageAckService.class).handleMessage(ClientUtils.getClientSocket(ctx), message);
                return;
            } else if (message.getMsgType() == MessageTypeEnum.Heartbeat) {
                AvalonApplicationContext.getInstance().getBean(MessageHeartBeatService.class).handleMessage(ClientUtils.getClientSocket(ctx), message);
                return;
            } else if (message.getMsgType() == MessageTypeEnum.Event) {
                AvalonApplicationContext.getInstance().getBean(MessageEventService.class).handleMessage(ClientUtils.getClientSocket(ctx), message);
                return;
            }

            if (message.getChatType() == ChatTypeEnum.Single) { // 单聊
                AvalonApplicationContext.getInstance().getBean(MessageSingleChatService.class).handleMessage(ClientUtils.getClientSocket(ctx), message);
            } else if (message.getChatType() == ChatTypeEnum.Group) {
                AvalonApplicationContext.getInstance().getBean(MessageTeamChatService.class).handleMessage(ClientUtils.getClientSocket(ctx), message);
            } else if (message.getChatType() == ChatTypeEnum.Custom) {
                AvalonApplicationContext.getInstance().getBean(MessageTeamChatService.class).handleMessage(ClientUtils.getClientSocket(ctx), message);
            }

        }
    }

    /**
     * 当客户端连接服务端之后（打开连接）
     *
     * @param ctx 上下文对象，含有管道pipeline，通道channel，地址
     * @param evt 事件对象，可以拿到事件的类型，和数据
     * @throws Exception
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        super.userEventTriggered(ctx, evt);
        if (evt instanceof io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler.HandshakeComplete) {
            log.info("客户端连接成功：{}", ClientUtils.getClientSocket(ctx));
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        ClientSocket clientSocket = ClientUtils.getClientSocket(ctx);
        log.info("有客户端断开连接：{}", clientSocket);
        ClientUtils.removeClientSocket(clientSocket);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error(String.format("异常：{}", cause.getMessage()), cause);
    }
}
