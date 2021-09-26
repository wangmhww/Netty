package com.wm.bio;

import lombok.SneakyThrows;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author wangm
 * @title: SocketServer
 * @projectName netty-parent
 * @description: TODO
 * @date 2021/6/2922:56
 */
public class SocketServer {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(9000);
        while (true) {
            System.out.println("等待连接。。。");
            Socket socket = serverSocket.accept();
            System.out.println("有客户端建立连接");
//            handler(socket);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        handler(socket);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }

    }
    public static void handler(Socket socket) throws IOException {
        System.out.println("准备读取。。。。");
        InputStream inputStream = socket.getInputStream();
        OutputStream outputStream = socket.getOutputStream();
        byte[] bytes = new byte[1024];

        while (true) {
            int read = inputStream.read(bytes);
            System.out.println("读取完毕");

            if (read != -1) {
                System.out.println("接受到客户端的数据：" + new String(bytes, 0, read));
            }
            outputStream.write("Server Date".getBytes());
            outputStream.flush();
        }
    }
}
