package com.wm.bio;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketAddress;

/**
 * @author wangm
 * @title: SocketClient
 * @projectName netty-parent
 * @description: TODO
 * @date 2021/6/2922:55
 */
public class SocketClient {
    public static void main(String[] args) throws IOException {
        InputStream in = null;
        OutputStream out = null;
        try {
            Socket socket = new Socket("localhost", 9000);

            in = socket.getInputStream();

            out = socket.getOutputStream();

            out.write("HelloServer".getBytes());
            out.flush();
            System.out.println("向服务端推送数据");
            byte[] bytes = new byte[1024];
            int read = in.read(bytes);
            if (read != -1) {
                System.out.println(new String(bytes, 0, read));
            }
        } finally {

            if (in != null) {
                in.close();
                in = null;
            }
            if (out != null) {
                out.close();
                out = null;
            }

        }


    }
}
