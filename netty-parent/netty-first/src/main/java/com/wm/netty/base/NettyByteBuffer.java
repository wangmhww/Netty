package com.wm.netty.base;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * @author wangm
 * @title: NettyByteBuffer
 * @projectName netty-parent
 * @description: TODO
 * @date 2021/6/2223:14
 */
public class NettyByteBuffer {
    public static void main(String[] args) {
        ByteBuf byteBuf = Unpooled.buffer(10);
        System.out.println("ByteBuf :" + byteBuf);

        for (int i = 0;i < 8; i++) {
            byteBuf.writeByte(i);
        }
        System.out.println("ByteBuf :" + byteBuf);

        for (int i = 0;i < 5; i ++) {
            System.out.println(byteBuf.getByte(i));
        }

        System.out.println("ByteBuf :" + byteBuf);

        for (int i = 0; i < 5; i++) {
            System.out.println(byteBuf.readByte());
        }

        System.out.println("ByteBuf :" + byteBuf);
    }
}
