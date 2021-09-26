package com.wm.netty.heartbbeat;

import com.wm.netty.chat.ChatServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

/**
 * @author wangm
 * @title: HeartBeatServer
 * @projectName netty-parent
 * @description: TODO
 * @date 2021/6/2723:16
 */
public class HeartBeatServer {
    public static void main(String[] args) throws InterruptedException {
        //创建两个线程组bossGroup和workerGroup, 含有的子线程NioEventLoop的个数默认为cpu核数的两倍
        // bossGroup只是处理连接请求 ,真正的和客户端业务处理，会交给workerGroup完成
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        ServerBootstrap bootstrap = new ServerBootstrap();
        try {
            bootstrap.group(bossGroup, workerGroup)  // 设置两个线程组
                    // 使用NioServerSocketChannel作为服务器的通道实现
                    .channel(NioServerSocketChannel.class)
                    // 初始化服务器连接队列大小，服务端处理客户端连接请求是顺序处理的，所以在一个时间只能处理一个客户端连接
                    // 多个客户端同时来的时候，服务端将不能处理的客户端连接请求放在队列中等待处理
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .childHandler(new ChannelInitializer<SocketChannel>() { //创建通道初始化对象，设置初始化参数
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            pipeline.addLast("decoder", new StringDecoder());
                            pipeline.addLast("encoder", new StringEncoder());
                            pipeline.addLast(new IdleStateHandler(3,0,0, TimeUnit.SECONDS));
                            // 对workGroup的socketChannel设置处理器
                            pipeline.addLast(new HeartBeatServerHandler());
                        }
                    });
            System.out.println("Netty server start ...");

            // 绑定一个端口并且同步，生成channelFuture异步对象通过isDone()等方法可以判断异步事件的执行情况
            // 启动 服务（并绑定端口），bind是异步操作，sync方法是等待异步执行完毕
            ChannelFuture cf = bootstrap.bind(9000).sync();

            // 等待服务端监听端口关闭，closeFuture是异步操作
            // 通过sync方法同步等待通知关闭处理完毕，这里会阻塞等待通道关闭完成，内部调用的是Object的wait（）方法
            cf.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
        }

    }
}
