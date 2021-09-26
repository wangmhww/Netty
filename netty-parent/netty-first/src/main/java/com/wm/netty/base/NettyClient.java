package com.wm.netty.base;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @author wangm
 * @title: NettyClient
 * @projectName netty-parent
 * @description: TODO
 * @date 2021/6/2220:31
 */
public class NettyClient {
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
                            // 对workGroup的socketChannel设置处理器
                            socketChannel.pipeline().addLast(new NettyClientHandler());
                        }
                    });
            System.out.println("netty client start ...");
            ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 9000).sync();
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            loopGroup.shutdownGracefully();
        }
    }
}
