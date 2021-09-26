package com.wm.aio;

import lombok.SneakyThrows;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

/**
 * @author wangm
 * @title: AIOServer
 * @projectName netty-parent
 * @description: TODO
 * @date 2021/7/623:21
 */
public class AIOServer {
    public static void main(String[] args) throws IOException, InterruptedException {
        final AsynchronousServerSocketChannel serverSocketChannel =
                AsynchronousServerSocketChannel.open().bind(new InetSocketAddress(9000));
        serverSocketChannel.accept(null, new CompletionHandler<AsynchronousSocketChannel, Object>() {

            @SneakyThrows
            @Override
            public void completed(AsynchronousSocketChannel socketChannel, Object attachment) {
                System.out.println("2---" + Thread.currentThread().getName());
                //  再次接受客户端连接，如果不写这块代码后面的客户端连不了服务端
                serverSocketChannel.accept(attachment,this);
                System.out.println(socketChannel.getRemoteAddress());
                ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

                socketChannel.read(byteBuffer, byteBuffer, new CompletionHandler<Integer, ByteBuffer>() {
                    @Override
                    public void completed(Integer result, ByteBuffer attachment) {
                        System.out.println("3---" + Thread.currentThread().getName());
                        byteBuffer.flip();
                        System.out.println(new String(byteBuffer.array(), 0, result));
                        socketChannel.write(ByteBuffer.wrap("HelloClient".getBytes()));
                    }

                    @Override
                    public void failed(Throwable exc, ByteBuffer attachment) {
                        exc.printStackTrace();
                    }
                });
            }

            @Override
            public void failed(Throwable exc, Object attachment) {
                exc.printStackTrace();
            }
        });
        System.out.println("1----" + Thread.currentThread().getName());
        Thread.sleep(Integer.MAX_VALUE);
    }
}
