package com.wm.netty.reconnect;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.util.Scanner;
import java.util.concurrent.TimeUnit;

/**
 * @author wangm
 * @title: NettyClient
 * @projectName netty-parent
 * @description: TODO
 * @date 2021/6/2220:31
 */
public class NettyClient {

    private String host;

    private int port;

    private Bootstrap bootStrap;

    private EventLoopGroup group;

    public static void main(String[] args) {
        NettyClient nettyClient = new NettyClient("localhost", 9000);
        nettyClient.connect();
    }

    public NettyClient(String host, int port) {
        this.host = host;
        this.port = port;
        this.init();
    }

    public void init () {
        this.group = new NioEventLoopGroup();

        // 注意客户端使用的不是ServerBootStrap而是Bootstrap
        this.bootStrap = new Bootstrap();

        this.bootStrap.group(this.group)
                    .channel(NioSocketChannel.class) // 使用NioSocketChannel作为客户端的通道实现
                    .handler(new ChannelInitializer<SocketChannel>() { //创建通道初始化对象，设置初始化参数
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline channelPipeline = socketChannel.pipeline();
                            // 对workGroup的socketChannel设置处理器
                            channelPipeline.addLast(new NettyClientHandler());
                        }
                    });
    }
    public void connect () {
        System.out.println("netty client start。。");
        ChannelFuture channelFuture = this.bootStrap.connect(this.host, this.port);
        channelFuture.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                if (!channelFuture.isSuccess()) {
                    channelFuture.channel().eventLoop().schedule(()->{
                        System.err.println("重连服务器。。。。");
                        try {
                            connect();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }, 3000, TimeUnit.MILLISECONDS);
                } else {
                    System.out.println("重连成功");
                }
            }
        });

    }
    //    public static void main(String[] args) {
//        // 客户端需要一个事件循环组
//        EventLoopGroup loopGroup = new NioEventLoopGroup();
//
//        try {
//            // 创建客户端启动对象
//            // 注意客户端使用的不是ServerBootStrap而是Bootstrap
//            Bootstrap bootstrap = new Bootstrap();
//
//            bootstrap.group(loopGroup)
//                    .channel(NioSocketChannel.class) // 使用NioSocketChannel作为客户端的通道实现
//                    .handler(new ChannelInitializer<SocketChannel>() { //创建通道初始化对象，设置初始化参数
//                        @Override
//                        protected void initChannel(SocketChannel socketChannel) throws Exception {
//                            ChannelPipeline channelPipeline = socketChannel.pipeline();
//                            channelPipeline.addLast(new DelimiterBasedFrameDecoder(1024, Unpooled.copiedBuffer("_".getBytes())));
//                            channelPipeline.addLast("decoder", new StringDecoder());
//                            channelPipeline.addLast("encoder", new StringEncoder());
//                            // 对workGroup的socketChannel设置处理器
//                            channelPipeline.addLast(new NettyClientHandler());
//                        }
//                    });
//            ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 9000).sync();
//            Channel channel = channelFuture.channel();
//            System.out.println("============" + channel.localAddress() + "================");
//            Scanner scanner = new Scanner(System.in);
////            while (scanner.hasNextLine()) {
////                String msg = scanner.nextLine();
////
////                channel.writeAndFlush(msg);
////            }
//
//            for (int i = 0; i < 200; i++) {
//                channel.writeAndFlush("hello 汪明" + "_");
//            }
//            channelFuture.channel().closeFuture().sync();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } finally {
//            loopGroup.shutdownGracefully();
//        }
//    }
}
