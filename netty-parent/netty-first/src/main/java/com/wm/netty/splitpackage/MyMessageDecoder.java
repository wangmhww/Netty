package com.wm.netty.splitpackage;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;

/**
 * @author wangm
 * @title: MyMessageDecode
 * @projectName netty-parent
 * @description: TODO
 * @date 2021/6/2722:40
 */
public class MyMessageDecoder extends ByteToMessageDecoder {

    int length = 0;

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf in, List<Object> out) throws Exception {
        System.out.println();
        System.out.println("MyMessageDecode decode 被调用");
        System.out.println(in);

        if (in.readableBytes() > 4) {
            if (length ==0) {
                length = in.readInt();
            }
            if (in.readableBytes() < length) {
                System.out.println("当前读取的数据不够，继续等待。。。");
                return;
            }
            byte[] content = new byte[length];
            if(in.readableBytes() >= length) {
                in.readBytes(content);

                MyMessageProtocol messageProtocol = new MyMessageProtocol();
                messageProtocol.setLen(length);
                messageProtocol.setContent(content);
                out.add(messageProtocol);
            }
            length = 0;
        }
    }
}

















