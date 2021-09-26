package com.wm.netty.heartbbeat;

import com.wm.netty.chat.ChatClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.util.Random;
import java.util.Scanner;

/**
 * @author wangm
 * @title: HeartBeatClient
 * @projectName netty-parent
 * @description: TODO
 * @date 2021/6/2723:44
 */
public class HeartBeatClient {
    public static void main(String[] args) {
        // 客户端需要一个事件循环组
        EventLoopGroup loopGroup = new NioEventLoopGroup();

        try {
            // 创建客户端启动对象
            // 注意客户端使用的不是ServerBootStrap而是Bootstrap
            Bootstrap bootstrap = new Bootstrap();

            bootstrap.group(loopGroup)
                    .channel(NioSocketChannel.class) // 使用NioSocketChannel作为客户端的通道实现
                    .handler(new ChannelInitializer<SocketChannel>() { //创建通道初始化对象，设置初始化参数
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline channelPipeline = socketChannel.pipeline();

                            channelPipeline.addLast("decoder", new StringDecoder());
                            channelPipeline.addLast("encoder", new StringEncoder());
                            // 对workGroup的socketChannel设置处理器
                            channelPipeline.addLast(new HeartBeatClientHandler());
                        }
                    });
            ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 9000).sync();
            Channel channel = channelFuture.channel();
            String text = "Heartbeat Packet";
            Random random = new Random();
            while (channel.isActive()) {
                int num = random.nextInt(8);
                Thread.sleep(5 * 1000);
                channel.writeAndFlush(text);
            }
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            loopGroup.shutdownGracefully();
        }
    }
}
