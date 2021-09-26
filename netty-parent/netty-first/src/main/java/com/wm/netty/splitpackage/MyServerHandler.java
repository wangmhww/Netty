package com.wm.netty.splitpackage;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.text.SimpleDateFormat;

/**
 * @author wangm
 * @title: MyServerHandler
 * @projectName netty-parent
 * @description: TODO
 * @date 2021/6/2722:31
 */
public class MyServerHandler extends SimpleChannelInboundHandler<MyMessageProtocol> {

    private int count = 0;
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, MyMessageProtocol msg) throws Exception {
        System.out.println("==============服务端接收到的消息如下===");
        System.out.println("长度=" + msg.getLen());
        System.out.println("内容=" + new String(msg.getContent(), CharsetUtil.UTF_8));
        System.out.println("服务端接收到的消息包数量=" + (++ this.count));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
