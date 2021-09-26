package com.wm.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author wangm
 * @title: NioServer
 * @projectName netty-parent
 * @description: TODO
 * @date 2021/6/2923:24
 */
public class NioServer {

    private static List<SocketChannel> channelList = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.socket().bind(new InetSocketAddress(9000));
        serverSocketChannel.configureBlocking(false);
        System.out.println("服务启动成功");
        while (true) {
            // NIO非阻塞是由操作系统内部实现的，底层调用了Linux内核的accept函数
            SocketChannel socketChannel = serverSocketChannel.accept();

            if (socketChannel != null) {
                System.out.println("连接建立成功");
                socketChannel.configureBlocking(false);
                channelList.add(socketChannel);

            }
            Iterator<SocketChannel> iterator = channelList.iterator();
            while (iterator.hasNext()) {
                SocketChannel sc = iterator.next();
                ByteBuffer byteBuffer = ByteBuffer.allocate(128);

                int len = sc.read(byteBuffer);
                if (len > 0) {
                    System.out.println("接收到数据" + new String(byteBuffer.array()));
                } else if (len == -1) {
                    iterator.remove();
                    System.out.println("客户端断开连接");
                }
            }
        }
    }
}
