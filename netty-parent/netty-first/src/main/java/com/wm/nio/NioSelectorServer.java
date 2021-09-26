package com.wm.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @author wangm
 * @title: NioSelectorServer
 * @projectName netty-parent
 * @description: TODO
 * @date 2021/6/3022:55
 */
public class NioSelectorServer {
    public static void main(String[] args) throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.socket().bind(new InetSocketAddress(9000));

        // 设置ServerSocketChannel为非阻塞
        serverSocketChannel.configureBlocking(false);
        // 打开Selector处理channel，即创建epoll
        Selector selector = Selector.open();

        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        System.out.println("服务器启动");
        while (true) {
            // 等待需要处理的事件发生
            selector.select();

            // 获取selector中注册的全部事件的SelectionKey实例
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();

            // 遍历SelectionKey对事件进行处理
            while (iterator.hasNext()) {
                SelectionKey selectionKey = iterator.next();
                // 如果是OP_ACCEPT事件，则进行连接获取或事件注册
                if (selectionKey.isAcceptable()) {
                    ServerSocketChannel server = (ServerSocketChannel) selectionKey.channel();
                    SocketChannel socketChannel = server.accept();
                    socketChannel.configureBlocking(false);
                    // 这里只注册了读事件，如果需要给客户端发送数据可以注册写事件
                    socketChannel.register(selector, SelectionKey.OP_READ);
                    System.out.println("客户端建立连接成功");
                } else if (selectionKey.isReadable()) {
                    SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                    ByteBuffer byteBuffer = ByteBuffer.allocate(128);
                    int len = socketChannel.read(byteBuffer);
                    if (len > 0 ) {
                        System.out.println("接收到消息：" + new String(byteBuffer.array()));

                    } else if (len == -1){
                        System.out.println("客户端断开连接");
                        socketChannel.close();
                    }
                }
                iterator.remove();
            }
        }
    }

}
