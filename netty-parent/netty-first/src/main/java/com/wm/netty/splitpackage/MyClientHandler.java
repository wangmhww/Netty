package com.wm.netty.splitpackage;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

/**
 * @author wangm
 * @title: MyClientHandler
 * @projectName netty-parent
 * @description: TODO
 * @date 2021/6/2722:31
 */
public class MyClientHandler extends SimpleChannelInboundHandler<String> {
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        for (int i = 0; i < 10; i++) {
            String msg = "您好， 我是张三！";
            MyMessageProtocol messageProtocol = new MyMessageProtocol();
            messageProtocol.setLen(msg.getBytes(CharsetUtil.UTF_8).length);
            messageProtocol.setContent(msg.getBytes(CharsetUtil.UTF_8));
            ctx.writeAndFlush(messageProtocol);
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String msg) throws Exception {

    }


}
