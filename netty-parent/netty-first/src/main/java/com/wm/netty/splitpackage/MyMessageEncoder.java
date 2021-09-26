package com.wm.netty.splitpackage;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author wangm
 * @title: MyMessageEncoder
 * @projectName netty-parent
 * @description: TODO
 * @date 2021/6/2722:37
 */
public class MyMessageEncoder extends MessageToByteEncoder<MyMessageProtocol> {

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, MyMessageProtocol msg, ByteBuf out) throws Exception {
        System.out.println("MyMessageEncoder encoder 方法别调用");
        out.writeInt(msg.getLen());
        out.writeBytes(msg.getContent());
    }
}
