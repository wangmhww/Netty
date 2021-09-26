package com.wm.netty.heartbbeat;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author wangm
 * @title: HeartBeatClientHandler
 * @projectName netty-parent
 * @description: TODO
 * @date 2021/6/2723:43
 */
public class HeartBeatClientHandler extends SimpleChannelInboundHandler<String> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String msg) throws Exception {
        String closeTest = "idle close";
        if (closeTest.equals(msg)) {
            channelHandlerContext.close();
        } else {
            System.out.println("[server] send message: " + msg);
        }
    }
}
