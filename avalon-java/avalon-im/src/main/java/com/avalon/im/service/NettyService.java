/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.im.service;

import com.avalon.core.context.Context;
import com.avalon.im.config.ImConfig;
import com.avalon.im.handler.ImWebSocketServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketServerCompressionHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyStore;
import java.util.concurrent.ThreadPoolExecutor;

@Slf4j
public class NettyService {
    public static void start(ThreadPoolExecutor threadPoolExecutor) {
        //创建两个线程组 boosGroup、workerGroup
        EventLoopGroup bossGroup = new NioEventLoopGroup(1, threadPoolExecutor);
        EventLoopGroup workerGroup = new NioEventLoopGroup(2, threadPoolExecutor);
        try {
            //创建服务端的启动对象，设置参数
            ServerBootstrap bootstrap = new ServerBootstrap();
            //设置两个线程组boosGroup和workerGroup
            bootstrap.group(bossGroup, workerGroup)
                    //设置服务端通道实现类型
                    .channel(NioServerSocketChannel.class)
                    //设置线程队列得到连接个数
                    .option(ChannelOption.SO_BACKLOG, 128)
                    //设置保持活动连接状态
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    //使用匿名内部类的形式初始化通道对象
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            //给pipeline管道设置处理器
                            ChannelPipeline pipeline = socketChannel.pipeline();

                            ImConfig config = Context.getAvalonApplicationContextInstance().getBean(ImConfig.class);
                            if (config.getWss()) {
                                KeyStore ks = KeyStore.getInstance("jks");
                                InputStream ksInputStream = new FileInputStream(
                                        NettyService.class.getResource("/955980.com.cn.jks").getPath());
                                ks.load(ksInputStream, "t4wlpnfo".toCharArray());
                                KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
                                kmf.init(ks, "t4wlpnfo".toCharArray());
                                SSLContext sslContext = SSLContext.getInstance("TLS");
                                sslContext.init(kmf.getKeyManagers(), null, null);
                                SSLEngine sslEngine = sslContext.createSSLEngine();
                                sslEngine.setUseClientMode(false);
                                sslEngine.setNeedClientAuth(false);
                                pipeline.addLast("sslHandler", new SslHandler(sslEngine));
                            }

                            pipeline.addLast(new IdleStateHandler(0, 0, 3600));
                            pipeline.addLast(new HttpServerCodec()); // HTTP 协议解析，用于握手阶段
                            pipeline.addLast(new HttpObjectAggregator(65536)); // HTTP 协议解析，用于握手阶段
                            pipeline.addLast(new WebSocketServerCompressionHandler()); // WebSocket 数据压缩扩展
                            pipeline.addLast(new WebSocketServerProtocolHandler("/ws", null, true)); // WebSocket 握手、控制帧处理
                            socketChannel.pipeline().addLast(new ImWebSocketServerHandler());
                        }
                    });//给workerGroup的EventLoop对应的管道设置处理器
            log.info("netty服务端启动成功");
            //绑定端口号，启动服务端
            ChannelFuture channelFuture = bootstrap.bind(6666).sync();
            //对关闭通道进行监听
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
